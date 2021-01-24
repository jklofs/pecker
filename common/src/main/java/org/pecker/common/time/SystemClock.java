package org.pecker.common.time;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.pecker.common.env.EnvUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class SystemClock {

    private static final SystemClock SYSTEM_CLOCK = new SystemClock();

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private volatile long diffTime = 0;

    private long maxDiffLastTime;

    private long maxConnectTime;

    private double maxVariance;

    private double maxOneVariance;

    private int sampleCount;

    private int maxFailureTimes;

    private int failureTimes;

    private int maxRetryCount;

    private boolean ntpEnable;

    private volatile long lastTime = System.currentTimeMillis();

    private Runnable runnable = ()->timeTask();

    @Getter
    private volatile boolean hasInit = false;

    private SystemClock(){
    }

    private static SystemClock getInstance(){
        if (!SYSTEM_CLOCK.hasInit){
            synchronized (SYSTEM_CLOCK){
                if (!SYSTEM_CLOCK.hasInit){
                    SYSTEM_CLOCK.init();
                }
            }
        }
        return SYSTEM_CLOCK;
    }

    public long getSystemClock() {
        long systemLocalTime = System.currentTimeMillis();
        long now = systemLocalTime + diffTime;
        if (now < lastTime&&  Math.abs(now - lastTime)> maxDiffLastTime) {
            synchronized (this) {
                now = System.currentTimeMillis() + diffTime;
                if ((Math.abs(now - lastTime)) > maxDiffLastTime) {
                    init();
                }
            }
        }
        setLastTime(now);
        return now;
    }

    public synchronized void init(){
        initProperties();
        if (ntpEnable) {
            boolean result = doTimeTask();
            if (!result) {
                for (int retryCount = 0; retryCount < maxRetryCount; retryCount++) {
                    if (doTimeTask()) {
                        return;
                    }
                }
            }
        }
        hasInit = true;
        if (!hasInit) {
            executorService.schedule(runnable, 15, TimeUnit.MINUTES);
        }
    }

    private void timeTask() {
        try {
            if (ntpEnable) {
                boolean result = doTimeTask();
                if (!result) {
                    if (failureTimes++ > maxFailureTimes) {
                        for (int retryCount = 0; retryCount < maxRetryCount; retryCount++) {
                            if (doTimeTask()) {
                                return;
                            }
                            failureTimes = 0;
                        }
                    }
                }
            }
        }catch (Exception e){

        }
    }

    private void initProperties(){
        maxDiffLastTime = NumberUtils.toInt(EnvUtils.getPropertyDefault("neo.maxDiffLastTime","500"));
        maxConnectTime = NumberUtils.toLong(EnvUtils.getPropertyDefault("neo.time.synchronization.maxConnectTime","3"));
        maxVariance = NumberUtils.toDouble(EnvUtils.getPropertyDefault("neo.time.synchronization.maxVariance","1"));
        maxOneVariance= NumberUtils.toDouble(EnvUtils.getPropertyDefault("neo.time.synchronization.maxOneVariance","1"));
        sampleCount= NumberUtils.toInt(EnvUtils.getPropertyDefault("neo.time.synchronization.sampleCount","1000"));
        maxFailureTimes= NumberUtils.toInt(EnvUtils.getPropertyDefault("neo.time.synchronization.maxFailureTimes","50"));
        failureTimes= NumberUtils.toInt(EnvUtils.getPropertyDefault("neo.time.synchronization.failureTimes","0"));
        maxRetryCount= NumberUtils.toInt(EnvUtils.getPropertyDefault("neo.time.synchronization.maxRetryCount","100"));
        ntpEnable= BooleanUtils.toBoolean(EnvUtils.getPropertyDefault("ntp.enable","true"));
    }

    private synchronized boolean doTimeTask() {
        int i = sampleCount;
        List<Long> resultList = new ArrayList<>(1000);
        while (i-- > 0) {
            long requestStartTime;
            long requestEndTime;
            requestStartTime = System.currentTimeMillis();
            long timestampResult = 0;
            int ntpTimes = 0;
            while (timestampResult == 0&&ntpTimes<maxRetryCount) {
                try {
                    timestampResult = NTPUtils.getTime();
                } catch (TimeoutException e) {
                }finally {
                    ntpTimes++;
                }
            }

            if (timestampResult == 0){
                continue;
            }
            requestEndTime = System.currentTimeMillis();
            //塞选掉连接请求时长过长的时间
            if (requestEndTime - requestStartTime > maxConnectTime) {
                continue;
            }
            resultList.add(timestampResult * 2 - requestStartTime - requestEndTime);
        }
        if (resultList.size()<=0){
            log.error("捕获不到ntp 时间同步数据");
            return false;
        }
        //塞选掉偏离过大的值
        BigDecimal sum = BigDecimal.ZERO;
        for (long diffTimeItem : resultList) {
            sum = sum.add(BigDecimal.valueOf(diffTimeItem));
        }
        BigDecimal vag = sum.divide(BigDecimal.valueOf(resultList.size()), 2, BigDecimal.ROUND_HALF_UP);
        Iterator<Long> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            long diffTimeItem = iterator.next();
            if (((BigDecimal.valueOf(diffTimeItem).subtract(vag)).multiply(BigDecimal
                    .valueOf(diffTimeItem).subtract(vag))).compareTo(BigDecimal.valueOf(maxOneVariance)) > 0) {
                iterator.remove();
            }
        }
        //如果样本值数量不满足需求则抛弃这次校准
        if (CollectionUtils.size(resultList) < (sampleCount / 20)) {
            return false;
        }
        //判断这个样本数据的离散情况，如果方差不满足需求则抛弃这次校准
        BigDecimal varianceTotal = BigDecimal.ZERO;
        for (long diffTimeItem : resultList) {
            varianceTotal = ((BigDecimal.valueOf(diffTimeItem).subtract(vag)).multiply(BigDecimal
                    .valueOf(diffTimeItem).subtract(vag))).add(varianceTotal);
        }
        BigDecimal variance = varianceTotal.divide(BigDecimal.valueOf(resultList.size()), 2, BigDecimal.ROUND_HALF_UP);
        if (variance.compareTo(BigDecimal.valueOf(maxVariance)) > 0) {
            return false;
        }
        sum = BigDecimal.ZERO;
        for (long diffTimeItem : resultList) {
            sum = sum.add(BigDecimal.valueOf(diffTimeItem));
        }
        long newDiffTime = sum.divide(BigDecimal.valueOf(resultList.size()), 2, BigDecimal.ROUND_HALF_UP).longValue();

        if (Math.abs(newDiffTime - diffTime) > 100) {
            this.diffTime = newDiffTime;
        }
        return true;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
