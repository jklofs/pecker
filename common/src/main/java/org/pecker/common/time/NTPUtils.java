package org.pecker.common.time;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
public class NTPUtils {
    private static NTPUDPClient client = new NTPUDPClient();
    private static InetAddress timeServerAddress;

    static {
        InetAddress temporary = null;
        try {
            temporary = InetAddress.getByName("time.huazhu.com");
        } catch (UnknownHostException e) {
            log.error("创建ntp服务访问地址失败");

        }
        timeServerAddress = temporary;
        client.setDefaultTimeout(30);
    }

    public static long getOffset() throws TimeoutException {
        TimeInfo timeInfo = getTimeInfo();
        timeInfo.computeDetails();
        long delay = timeInfo.getOffset();
        return delay;
    }

    public static Date getDate() throws TimeoutException {
        long time = getTime();
        return new Date(time);
    }

    public static long getTime() throws TimeoutException {
        TimeInfo timeInfo = getTimeInfo();
        long time = timeInfo.getMessage().getReceiveTimeStamp().getTime();
        return time;
    }

    private static TimeInfo getTimeInfo() throws TimeoutException {
        return getTimeInfo(0);
    }

    private static TimeInfo getTimeInfo(int i) throws TimeoutException {
        try {
          TimeInfo timeInfo = client.getTime(timeServerAddress);
          return timeInfo;
        } catch (Exception e) {
            log.warn("ntp error:{}",e.getMessage());
            throw new TimeoutException("ntp time out");
        }
    }

    @SneakyThrows
    public static void main(String[] args) {

       String tt = DateUtil.format(DateUtil.CHINA_DATE_TIME,NTPUtils.getDate());
       System.out.println(tt);
        int i= 1000;
        List<Long> resultList = new ArrayList<>(1000);
        while (i-->0) {
            long requestStartTime;
            long requestEndTime;
            requestStartTime= System.currentTimeMillis();
            long timestampResult = 0;
            while (timestampResult == 0) {
                try {
                    timestampResult = NTPUtils.getTime();
                } catch (TimeoutException e) {
                }
            }
            requestEndTime=System.currentTimeMillis();
            //塞选掉连接请求时长过长的时间
            if (requestEndTime-requestStartTime>100){
                continue;
            }
            resultList.add(timestampResult*2 - requestStartTime - requestEndTime);
        }
        //塞选掉偏离过大的值
        BigDecimal sum = BigDecimal.ZERO;
        for (long diffTimeItem : resultList){
            sum = sum.add(BigDecimal.valueOf(diffTimeItem));
        }
        BigDecimal vag = sum.divide(BigDecimal.valueOf(resultList.size()),2,BigDecimal.ROUND_HALF_UP);
        Iterator<Long> iterator = resultList.iterator();
        while (iterator.hasNext()){
            long diffTimeItem = iterator.next();
            if (((BigDecimal.valueOf(diffTimeItem).subtract(vag)).multiply(BigDecimal
                    .valueOf(diffTimeItem).subtract(vag))).compareTo(BigDecimal.valueOf(1.2))>0){
                iterator.remove();
            }
        }
        //如果样本值数量不满足需求则抛弃这次校准
        if (CollectionUtils.size(resultList)<(50/2)){
            System.out.println("32131:"+CollectionUtils.size(resultList));
            return ;
        }
        //判断这个样本数据的离散情况，如果方差不满足需求则抛弃这次校准
        BigDecimal varianceTotal = BigDecimal.ZERO;
        for (long diffTimeItem : resultList){
            varianceTotal = ((BigDecimal.valueOf(diffTimeItem).subtract(vag)).multiply(BigDecimal
                    .valueOf(diffTimeItem).subtract(vag))).add(varianceTotal);
        }
        BigDecimal variance = varianceTotal.divide(BigDecimal.valueOf(resultList.size()),2,BigDecimal.ROUND_HALF_UP);
        if (variance.compareTo(BigDecimal.valueOf(1.2))>0){
            System.out.println("asaas");
            return ;
        }
        sum = BigDecimal.ZERO;
        for (long diffTimeItem : resultList){
            sum = sum.add(BigDecimal.valueOf(diffTimeItem));
        }
        long newDiffTime = sum.divide(BigDecimal.valueOf(resultList.size()),2,BigDecimal.ROUND_HALF_UP).longValue();
        System.out.println(newDiffTime);

        long now = System.currentTimeMillis();
        long time = getTime();
        long affter = System.currentTimeMillis();
        System.out.println(time*2-now-affter);
//        TimeInfo timeInfo = getTimeInfo();
//        timeInfo.computeDetails();
//        long offset = timeInfo.getOffset();
//        System.out.println(offset);
    }
}
