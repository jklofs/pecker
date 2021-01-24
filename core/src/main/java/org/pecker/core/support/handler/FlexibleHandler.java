package org.pecker.core.support.handler;

import com.esotericsoftware.reflectasm.MethodAccess;

public interface FlexibleHandler {

    Object invoke(Object proxy, MethodAccess method, Object[] args);
}
