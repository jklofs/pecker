package org.pecker.core.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.pecker.core.handler.FlexibleHandler;
import org.pecker.core.support.annotation.Flexible;
import org.pecker.core.support.tally.UnsafeCountBO;
import org.pecker.proxy.reflect.InvincibleMethod;

import java.lang.reflect.Method;

@Slf4j
public class FlexibleHandlerImpl implements FlexibleHandler {

    private final ThreadLocal<UnsafeCountBO> AROUND_TASK_NUM_THREAD_LOCAL = ThreadLocal.withInitial(()->new UnsafeCountBO());

    private Object instance;

    public FlexibleHandlerImpl(Object instance) {
        this.instance = instance;
    }

    @Override
    public boolean shouldProxy(Method method) {
        Flexible flexible = method.getAnnotation(Flexible.class);
        return flexible!=null;
    }

    @Override
    public Object invoke(Object proxy, InvincibleMethod method, Object... args) throws Throwable {
        try {
            UnsafeCountBO aroundTaskNum = AROUND_TASK_NUM_THREAD_LOCAL.get();
            aroundTaskNum.addAndGet();
            Object result = method.invoke(instance,args);
            aroundTaskNum.decrementAndGet();
            if (aroundTaskNum.get()==0) {
                AROUND_TASK_NUM_THREAD_LOCAL.remove();
            }
            return result;
        }catch (Throwable e){
            try {
                //TODO 错误处理
            }finally {
                AROUND_TASK_NUM_THREAD_LOCAL.remove();
                log.error("执行失败清除缓存：{}",e);
            }
            throw e;
        }
    }
}
