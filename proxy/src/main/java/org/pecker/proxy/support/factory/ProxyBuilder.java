package org.pecker.proxy.support.factory;

import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.pecker.common.code.CodeUtils;
import org.pecker.proxy.reflect.InvincibleMethod;
import org.pecker.proxy.support.BeanCreateProxy;
import org.pecker.proxy.support.MethodSign;
import org.pecker.proxy.support.handler.AroundProxyHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ProxyBuilder {
    private static final String PREFIX_PACKAGE = "org.pecker.proxy.support.factory.";

    private static final ClassPool pool = ClassPool.getDefault();

    private String name;

    private Class superClass;

    private List<Class> interfaceList;

    private ProxyConditionFilter conditionFilter;

    public ProxyBuilder(Class superClass, ProxyConditionFilter conditionFilter) {
        this(superClass,null,conditionFilter);
    }

    public ProxyBuilder(Class superClass, List<Class> interfaceList, ProxyConditionFilter conditionFilter) {
        this(superClass.getName()+"$Proxy",superClass,interfaceList,conditionFilter);
    }

    public ProxyBuilder(String name, Class superClass, List<Class> interfaceList, ProxyConditionFilter conditionFilter) {
        this.name = name;
        this.superClass = superClass;
        this.interfaceList = interfaceList;
        this.conditionFilter = conditionFilter;
    }

    public BeanCreateProxy build() throws NotFoundException, CannotCompileException, IOException {
        CtClass ctClass = pool.makeClass(PREFIX_PACKAGE + name);
        List<MethodSign> methodSignSet = new ArrayList<>();
        List<Class> constructorNeedClassList = new ArrayList<>();
        constructorNeedClassList.add(AroundProxyHandler.class);
        ctClass.addField(new CtField(pool.get(AroundProxyHandler.class.getName()), "handler", ctClass));

        constructorNeedClassList.add(InvincibleMethod[].class);
        ctClass.addField(new CtField(pool.get(InvincibleMethod[].class.getName()), "methods", ctClass));

        constructorNeedClassList.add(superClass);
        ctClass.addField(new CtField(pool.get(superClass.getName()), "instance", ctClass));
        int methodIndex = 0;
        if (superClass != null) {
            analysisSuperClass(superClass, ctClass, methodSignSet, methodIndex, true);
        }

        if (CollectionUtils.isNotEmpty(interfaceList)) {
            ctClass.setInterfaces(interfaceList.toArray(new CtClass[0]));
            for (Class interfaceClass : interfaceList) {
                analysisSuperClass(interfaceClass, ctClass, methodSignSet, methodIndex, false);
            }
        }
        ctClass.addConstructor(CtNewConstructor.make(changeClassToCtClass(constructorNeedClassList.toArray(new Class[0]))
                , null, "{ handler=$1; methods=$2; instance=$3; }", ctClass));
        return new BeanCreateProxy(ctClass.toClass(), methodSignSet, constructorNeedClassList);
    }

    private void analysisSuperClass(Class superClass, CtClass ctClass, List<MethodSign> methodSignList
            , int methodIndex, boolean isSuper) throws CannotCompileException, NotFoundException {
        if (isSuper) {
            ctClass.setSuperclass(pool.get(superClass.getName()));
        } else {
            ctClass.addInterface(pool.get(superClass.getName()));
        }
        for (Method method : superClass.getMethods()) {
            MethodSign methodSign = new MethodSign(method.getName(), method.getParameterTypes());
            if (methodSignList.stream().anyMatch(item -> item.equals(methodSign))) {
                continue;
            }
            if (Modifier.isFinal(method.getModifiers())) {
                continue;
            }
            methodSignList.add(methodSign);
            StringBuilder methodBodyBuilder = new StringBuilder();
            if (!conditionFilter.shouldProxy(method)) {
                methodBodyBuilder.append(CodeUtils.fillMethodReturnCode(method.getReturnType(),method.getReturnType()
                        , StringUtils.join("instance.",method.getName(),"($$)")));
            } else {
                methodBodyBuilder.append(CodeUtils.fillMethodReturnCode(Object.class,method.getReturnType()
                        ," (handler.invoke((java.lang.Object)$0,methods["+ (methodIndex++)+"],$args))"));
            }
            log.debug("method info {}",methodBodyBuilder.toString());
            ctClass.addMethod(CtNewMethod.make(pool.get(method.getReturnType().getName()), method.getName()
                    , changeClassToCtClass(method.getParameterTypes()), changeClassToCtClass(method.getExceptionTypes()), methodBodyBuilder.toString(), ctClass));
        }
    }

    private CtClass[] changeClassToCtClass(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes).map(item -> {
            try {
                return pool.get(item.getName());
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }).toArray(CtClass[]::new);
    }
}
