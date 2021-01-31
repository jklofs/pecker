package org.pecker.proxy.reflect;

public interface InvincibleMethod {
    Object invoke(Object instance,Object... args) throws Exception;

    String getName();

    int getIndex();

    Class[] getParameterTypes();
}
