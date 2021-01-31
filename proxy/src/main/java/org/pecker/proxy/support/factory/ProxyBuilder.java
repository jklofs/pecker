package org.pecker.proxy.support.factory;

import javassist.*;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.pecker.proxy.reflect.InvincibleMethod;
import org.pecker.proxy.support.BeanCreateProxy;
import org.pecker.proxy.support.MethodSign;
import org.pecker.proxy.support.handler.ProxyHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@AllArgsConstructor
public class ProxyBuilder {
    private static final String PREFIX_PACKAGE = "org.pecker.proxy.support.factory.";

    private static final ClassPool pool = ClassPool.getDefault();

    private String name;

    private Class superClass;

    private List<Class> interfaceList;

    private ProxyConditionFilter conditionFilter;

    public BeanCreateProxy build() throws NotFoundException, CannotCompileException, IOException {
        CtClass ctClass = pool.makeClass(PREFIX_PACKAGE + name);
        List<MethodSign> methodSignSet = new ArrayList<>();
        List<Class> constructorNeedClassList = new ArrayList<>();
        ctClass.addField(new CtField(pool.get(ProxyHandler.class.getName()),"handler",ctClass));
        constructorNeedClassList.add(ProxyHandler.class);
        ctClass.addField(new CtField(pool.get(InvincibleMethod[].class.getName()),"methods",ctClass));
        constructorNeedClassList.add(InvincibleMethod[].class);
        int methodIndex =0;
        if (superClass!=null) {
            analysisSuperClass(superClass,ctClass, methodSignSet, constructorNeedClassList, methodIndex,true);
        }

        if (CollectionUtils.isNotEmpty(interfaceList)) {
            ctClass.setInterfaces(interfaceList.toArray(new CtClass[0]));
            for (Class interfaceClass : interfaceList){
                analysisSuperClass(interfaceClass,ctClass, methodSignSet, constructorNeedClassList, methodIndex,false);
            }
        }
        StringBuilder constructorNeedBuilder = new StringBuilder();
        constructorNeedBuilder.append("{ handler=$1; methods=$2;");
        for (int index = 2 ;index<constructorNeedClassList.size();index++){
            Class constructorNeedClass = constructorNeedClassList.get(index);
            constructorNeedBuilder.append(constructorNeedClass.getSimpleName()).append("=").append("$"+(index+1)).append(";");
        }
        constructorNeedBuilder.append("}");
        ctClass.addConstructor(CtNewConstructor.make(changeClassToCtClass(constructorNeedClassList.toArray(new Class[0]))
                ,null,constructorNeedBuilder.toString(),ctClass));
        return new BeanCreateProxy(ctClass.toClass(),methodSignSet,constructorNeedClassList);
    }

    private void analysisSuperClass(Class superClass,CtClass ctClass, List<MethodSign> methodSignList
            , List<Class> constructorNeedClassList, int methodIndex,boolean isSuper) throws CannotCompileException, NotFoundException {
        constructorNeedClassList.add(superClass);
        if (isSuper) {
            ctClass.setSuperclass(pool.get(superClass.getName()));
        }else {
            ctClass.addInterface(pool.get(superClass.getName()));
        }
        ctClass.addField(new CtField(pool.get(superClass.getName()),superClass.getSimpleName(), ctClass));
        for (Method method : superClass.getMethods()){
            MethodSign methodSign = new MethodSign(method.getName(),method.getParameterTypes());
            if (methodSignList.stream().anyMatch(item->item.equals(methodSign))){
                continue;
            }
            if (Modifier.isFinal(method.getModifiers())){
                continue;
            }
            methodSignList.add(methodSign);
            StringBuilder methodBodyBuilder = new StringBuilder();
            if (!conditionFilter.shouldProxy(method)){
                methodBodyBuilder.append("return ($r)").append(superClass.getSimpleName()).append(".").append(method.getName()).append("($$);");
            }else {
                methodBodyBuilder.append("return ($r)(handler.invoke((java.lang.Object)$0,methods[").append(methodIndex++).append("],$args));");
            }
            ctClass.addMethod(CtNewMethod.make(pool.get(method.getReturnType().getName()),method.getName()
                    , changeClassToCtClass(method.getParameterTypes()), changeClassToCtClass(method.getExceptionTypes()),methodBodyBuilder.toString(), ctClass));
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

    public static class User{
        public void test(String a){
            a="1123";
            return;
        }
    }

    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProxyBuilder builder = new ProxyBuilder("UserPeox", User.class, null, new ProxyConditionFilter() {
            @Override
            public boolean shouldProxy(Method method) {
                return true;
            }
        });
        User user = new User();
        User proxy = (User) builder.build().createInstance(new ProxyHandler() {

            @Override
            public Object invoke(Object proxy, InvincibleMethod method, Object... args) {

                try {
                    return method.invoke(user,args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        },user);
        long now = System.currentTimeMillis();
        for (long i = 0 ;i<10000000010L;i++) {
            proxy.test("12121");
        }
        System.out.println(System.currentTimeMillis()-now);

        now = System.currentTimeMillis();
        for (long i = 0 ;i<10000000010L;i++) {
            user.test("12121");
        }
        System.out.println(System.currentTimeMillis()-now);
    }
}
