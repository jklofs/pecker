package org.pecker.proxy.support;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyBuilder {
    private static final String PREFIX_PACKAGE = "org.pecker.proxy.support.";

    private static final ClassPool pool = ClassPool.getDefault();

    private String name;

    private Class superClass;

    private List<Class> interfaceList;

    private ProxyConditionFilter conditionFilter;

    public Object build() throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.makeClass(PREFIX_PACKAGE + name);

        if (superClass!=null) {
            ctClass.setSuperclass(pool.get(superClass.getName()));
            for (Method method : superClass.getMethods()){

            }
        }

        if (CollectionUtils.isNotEmpty(interfaceList)) {
            ctClass.setInterfaces(interfaceList.toArray(new CtClass[0]));
        }

        return null;
    }
}
