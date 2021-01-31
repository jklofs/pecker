package org.pecker.proxy.support;

import java.lang.reflect.Method;

public interface ProxyConditionFilter {
    boolean shouldProxy(Method method);
}
