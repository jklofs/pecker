package org.pecker.proxy.support.factory;

import java.lang.reflect.Method;

public interface ProxyConditionFilter {
    boolean shouldProxy(Method method);
}
