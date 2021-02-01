package org.pecker.proxy.reflect;

import javassist.*;
import org.pecker.common.code.CodeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvincibleMethodFactory {

    private static final String PREFIX_PACKAGE = "org.pecker.proxy.reflect.";

    private static final Map<Class,List<InvincibleMethod>> cacheMethod = new ConcurrentHashMap<>();

    private static final ClassPool pool = ClassPool.getDefault();

    public static <T> List<InvincibleMethod> create(Class sourceClass) {
        return cacheMethod.computeIfAbsent(sourceClass,key->{
            try {
                Method[] methods = sourceClass.getMethods();
                if (methods == null || methods.length == 0) {
                    return new ArrayList<>(0);
                }
                List<InvincibleMethod> result = new ArrayList<>(methods.length);
                int index = 0;
                for (Method method : methods) {
                    CtClass ctClass = pool.makeClass(PREFIX_PACKAGE + sourceClass.getName() + "$" + method.getName() + index);
                    ctClass.addConstructor(CtNewConstructor.make(null, null, "{}", ctClass));
                    ctClass.addInterface(pool.get(InvincibleMethod.class.getName()));
                    StringBuilder builder = new StringBuilder();
                    builder.append("{");
                    builder.append(CodeUtils.fillMethodReturnCode(method.getReturnType(),Object.class,fillMethodUseCode(sourceClass, method)));
                    builder.append("}");
                    CtField ctField = new CtField(pool.get(Class[].class.getName()), "x", ctClass);
                    ctClass.addField(ctField);
                    ctClass.addConstructor(CtNewConstructor.make(new CtClass[]{pool.get(Class[].class.getName())}, null, "{x=$1;}", ctClass));
                    ctClass.addMethod(CtNewMethod.make(pool.get(Object.class.getName()), "invoke"
                            , new CtClass[]{pool.get(Object.class.getName()), pool.get(Object[].class.getName())}
                            , new CtClass[]{pool.get(Exception.class.getName())}, builder.toString(), ctClass));
                    ctClass.addMethod(CtNewMethod.make(pool.get(String.class.getName()), "getName"
                            , null, null, "{return \"" + method.getName() + "\";}", ctClass));
                    ctClass.addMethod(CtNewMethod.make(pool.get(int.class.getName()), "getIndex"
                            , null, null, "{return " + (index++) + ";}", ctClass));
                    ctClass.addMethod(CtNewMethod.getter("getParameterTypes", ctField));
                    Class cl = ctClass.toClass();
                    InvincibleMethod invincibleMethod = (InvincibleMethod) cl.getDeclaredConstructor(Class[].class).newInstance(new Object[]{method.getParameterTypes()});
                    result.add(invincibleMethod);
                }
                return result;
            }catch (CannotCompileException| NotFoundException| NoSuchMethodException| IllegalAccessException| InvocationTargetException| InstantiationException exception){
                throw new RuntimeException(exception);
            }
        });
    }

    private static String fillMethodUseCode(Class sourceClass, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(" (").append("(").append(sourceClass.getName()).append(")").append("$1").append(")")
                .append(".").append(method.getName()).append("(");
        Class[] parameterTypes = method.getParameterTypes();
        if (parameterTypes!= null && parameterTypes.length>0) {
            for (int i = 0 ; i<parameterTypes.length;i++) {
                Class parameterType = parameterTypes[i];
                if (parameterType.equals(int.class)) {
                    builder.append("(").append("(").append(Integer.class.getName()).append(")").append("$2[").append(i).append("]").append(").intValue()");
                } else if (parameterType.equals(double.class)) {
                    builder.append("(").append("(").append(Double.class.getName()).append(")").append("$2[").append(i).append("]").append(").doubleValue()");
                } else if (parameterType.equals(float.class)) {
                    builder.append("(").append("(").append(Float.class.getName()).append(")").append("$2[").append(i).append("]").append(").floatValue()");
                } else if (parameterType.equals(byte.class)) {
                    builder.append("(").append("(").append(Byte.class.getName()).append(")").append("$2[").append(i).append("]").append(").byteValue()");
                } else if (parameterType.equals(long.class)) {
                    builder.append("(").append("(").append(Long.class.getName()).append(")").append("$2[").append(i).append("]").append(").longValue()");
                } else if (parameterType.equals(boolean.class)) {
                    builder.append("(").append("(").append(Boolean.class.getName()).append(")").append("$2[").append(i).append("]").append(").booleanValue()");
                } else {
                    builder.append("(").append("(").append(parameterType.getName()).append(")").append("$2[").append(i).append("]").append(")");
                }
                if (parameterTypes.length-i!=1){
                    builder.append(",");
                }
            }
        }
        builder.append(")");
        return builder.toString();
    }
}
