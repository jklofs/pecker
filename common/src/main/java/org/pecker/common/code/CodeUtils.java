package org.pecker.common.code;

public class CodeUtils {

    public static final String fillMethodReturnCode(Class sourceType,Class targetType,String code){
        StringBuilder builder = new StringBuilder();
        if (!sourceType.equals(void.class) && !sourceType.equals(Void.class) && !targetType.equals(void.class) && !targetType.equals(Void.class)) {
            builder.append("return ");
            builder.append(cast(sourceType, targetType, code)).append(";");
        } else {
            builder.append(code);
            builder.append(";");
            if (!targetType.equals(void.class) && !targetType.equals(Void.class)) {
                builder.append("return null;");
            }
        }
        System.out.println(builder.toString());
        return builder.toString();
    }

    public static String cast(Class sourceType,Class targetType,String code){
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        if (targetType.equals(int.class)) {
            if (sourceType.equals(Integer.class)) {
                builder.append(code).append(".intValue()");
            } else if (sourceType.equals(int.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Integer.class.getName()).append(")").append(code).append(").intValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(long.class)) {
            if (sourceType.equals(Long.class)) {
                builder.append(code).append(".longValue()");
            } else if (sourceType.equals(long.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Long.class.getName()).append(")").append(code).append(").longValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(float.class)) {
            if (sourceType.equals(Float.class)) {
                builder.append(code).append(".floatValue()");
            } else if (sourceType.equals(float.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Float.class.getName()).append(")").append(code).append(").floatValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(double.class)) {
            if (sourceType.equals(Double.class)) {
                builder.append(code).append(".doubleValue()");
            } else if (sourceType.equals(double.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Double.class.getName()).append(")").append(code).append(").doubleValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(boolean.class)) {
            if (sourceType.equals(Boolean.class)) {
                builder.append(code).append(".booleanValue()");
            } else if (sourceType.equals(boolean.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Boolean.class.getName()).append(")").append(code).append(").booleanValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(byte.class)) {
            if (sourceType.equals(Byte.class)) {
                builder.append(code).append(".byteValue()");
            } else if (sourceType.equals(byte.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Byte.class.getName()).append(")").append(code).append(").byteValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(short.class)) {
            if (sourceType.equals(Short.class)) {
                builder.append(code).append(".shortValue()");
            } else if (sourceType.equals(short.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Short.class.getName()).append(")").append(code).append(").shortValue()");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Integer.class)) {
            if (sourceType.equals(int.class)) {
                builder.append(" new ").append(Integer.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Integer.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Integer.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Long.class)) {
            if (sourceType.equals(long.class)) {
                builder.append(" new ").append(Long.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Long.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Long.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Float.class)) {
            if (sourceType.equals(float.class)) {
                builder.append(" new ").append(Float.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Float.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Float.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Double.class)) {
            if (sourceType.equals(double.class)) {
                builder.append(" new ").append(Double.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Double.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Double.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Boolean.class)) {
            if (sourceType.equals(boolean.class)) {
                builder.append(" new ").append(Boolean.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Boolean.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Boolean.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Byte.class)) {
            if (sourceType.equals(byte.class)) {
                builder.append(" new ").append(Byte.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Byte.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Byte.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else if (targetType.equals(Short.class)) {
            if (sourceType.equals(short.class)) {
                builder.append(" new ").append(Short.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Short.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Short.class.getName()).append(")").append(code).append(")");
            }else {
                throw new ClassCastException();
            }
        }else {
            if (sourceType.isAssignableFrom(Object.class) && targetType.isAssignableFrom(Object.class)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }
        builder.append(")");
        return builder.toString();

    }
}
