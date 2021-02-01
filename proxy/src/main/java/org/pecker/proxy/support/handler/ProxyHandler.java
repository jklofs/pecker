package org.pecker.proxy.support.handler;

import org.pecker.proxy.reflect.InvincibleMethod;

import java.lang.reflect.InvocationHandler;

public interface ProxyHandler {

    Object invoke(Object proxy, InvincibleMethod method,Object... args) throws Throwable;
}
