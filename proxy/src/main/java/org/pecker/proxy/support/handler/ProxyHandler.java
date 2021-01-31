package org.pecker.proxy.support.handler;

import org.pecker.proxy.reflect.InvincibleMethod;

public interface ProxyHandler {

    Object invoke(Object proxy, InvincibleMethod method,Object... args);
}
