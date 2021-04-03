package org.pecker.core.handler.impl;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import junit.framework.TestCase;
import org.pecker.core.handler.FlexibleHandler;
import org.pecker.proxy.support.factory.ProxyBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FlexibleHandlerImplTest extends TestCase {



    public void testInvoke() throws IOException, CannotCompileException, NotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestServiceBO testServiceBO = new TestServiceBO();
        FlexibleHandler flexibleHandler = new FlexibleHandlerImpl(testServiceBO);
        ProxyBuilder builder = new ProxyBuilder(TestServiceBO.class,flexibleHandler);
        TestServiceBO proxy = (TestServiceBO) builder.build().createInstance(flexibleHandler,testServiceBO);
        System.out.println(proxy.playGame());
    }
}