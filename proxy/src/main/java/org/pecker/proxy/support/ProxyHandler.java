package org.pecker.proxy.support;

import org.pecker.proxy.reflect.InvincibleMethod;

public interface ProxyHandler {

    Object invoke(Object proxy, InvincibleMethod method,Object... args);
}
