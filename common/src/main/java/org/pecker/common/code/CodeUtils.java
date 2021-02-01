package org.pecker.common.code;

public class CodeUtils {

    public static final String fillMethodReturnCode(Class sourceType,Class targetType,String code){
        StringBuilder builder = new StringBuilder();
        if (!sourceType.equals(void.class) && !sourceType.equals(Void.class) && !targetType.equals(void.class) && !targetType.equals(Void.class)) {
            builder.append("return ");
            builder.append(cast(sourceType, targetType, code,true)).append(";");
        } else {
            builder.append(code);
            builder.append(";");
            if (!targetType.equals(void.class) && !targetType.equals(Void.class)) {
                if (targetType.isAssignableFrom(Object.class)) {
                    builder.append("return null;");
                }else if (targetType.equals(boolean.class)){
                    builder.append("return false;");
                }else {
                    builder.append("return 0x00;");
                }
            }
        }
        return builder.toString();
    }

    /**
     * 进行类型转换，自动拆箱装箱
     * 不用$r是因为$r在debug时会重复执行代码
     *
     * @param sourceType
     * @param targetType
     * @param code
     * @param isForce
     * @return
     */
    public static String cast(Class sourceType,Class targetType,String code,boolean isForce){
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        if (targetType.equals(int.class)) {
            if (sourceType.equals(Integer.class)) {
                builder.append(code).append(".intValue()");
            } else if (sourceType.equals(int.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Integer.class.getName()).append(")").append(code).append(").intValue()");
            }else if (!isForce) {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(long.class)) {
            if (sourceType.equals(Long.class)) {
                builder.append(code).append(".longValue()");
            } else if (sourceType.equals(long.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Long.class.getName()).append(")").append(code).append(").longValue()");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(float.class)) {
            if (sourceType.equals(Float.class)) {
                builder.append(code).append(".floatValue()");
            } else if (sourceType.equals(float.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Float.class.getName()).append(")").append(code).append(").floatValue()");
            }else if (!isForce){
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(double.class)) {
            if (sourceType.equals(Double.class)) {
                builder.append(code).append(".doubleValue()");
            } else if (sourceType.equals(double.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Double.class.getName()).append(")").append(code).append(").doubleValue()");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(boolean.class)) {
            if (sourceType.equals(Boolean.class)) {
                builder.append(code).append(".booleanValue()");
            } else if (sourceType.equals(boolean.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Boolean.class.getName()).append(")").append(code).append(").booleanValue()");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(byte.class)) {
            if (sourceType.equals(Byte.class)) {
                builder.append(code).append(".byteValue()");
            } else if (sourceType.equals(byte.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Byte.class.getName()).append(")").append(code).append(").byteValue()");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(short.class)) {
            if (sourceType.equals(Short.class)) {
                builder.append(code).append(".shortValue()");
            } else if (sourceType.equals(short.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Short.class.getName()).append(")").append(code).append(").shortValue()");
            }else if (!isForce) {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Integer.class)) {
            if (sourceType.equals(int.class)) {
                builder.append(" new ").append(Integer.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Integer.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Integer.class.getName()).append(")").append(code).append(")");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Long.class)) {
            if (sourceType.equals(long.class)) {
                builder.append(" new ").append(Long.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Long.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Long.class.getName()).append(")").append(code).append(")");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Float.class)) {
            if (sourceType.equals(float.class)) {
                builder.append(" new ").append(Float.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Float.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Float.class.getName()).append(")").append(code).append(")");
            }else if (!isForce) {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Double.class)) {
            if (sourceType.equals(double.class)) {
                builder.append(" new ").append(Double.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Double.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Double.class.getName()).append(")").append(code).append(")");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Boolean.class)) {
            if (sourceType.equals(boolean.class)) {
                builder.append(" new ").append(Boolean.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Boolean.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Boolean.class.getName()).append(")").append(code).append(")");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Byte.class)) {
            if (sourceType.equals(byte.class)) {
                builder.append(" new ").append(Byte.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Byte.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Byte.class.getName()).append(")").append(code).append(")");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Short.class)) {
            if (sourceType.equals(short.class)) {
                builder.append(" new ").append(Short.class.getName()).append("(").append(code).append(")");
            } else if (sourceType.equals(Short.class)) {
                builder.append(code);
            }else if (sourceType.equals(Object.class)){
                builder.append("(").append("(").append(Short.class.getName()).append(")").append(code).append(")");
            }else if (!isForce)  {
                throw new ClassCastException();
            }else if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }else if (targetType.equals(Object.class) && !sourceType.isAssignableFrom(Object.class)) {
            if (sourceType.equals(int.class)) {
                builder.append(" new ").append(Integer.class.getName());
            } else if (sourceType.equals(float.class)) {
                builder.append(" new ").append(Float.class.getName());
            } else if (sourceType.equals(long.class)) {
                builder.append(" new ").append(Long.class.getName());
            } else if (sourceType.equals(double.class)) {
                builder.append(" new ").append(Double.class.getName());
            } else if (sourceType.equals(byte.class)) {
                builder.append(" new ").append(Byte.class.getName());
            } else if (sourceType.equals(boolean.class)) {
                builder.append(" new ").append(Boolean.class.getName());
            } else if (sourceType.equals(short.class)) {
                builder.append(" new ").append(Short.class.getName());
            }
            builder.append("(").append(code).append(")");
        }else {
            if (!sourceType.equals(targetType)){
                builder.append("(").append("(").append(targetType.getName()).append(")").append(code).append(")");
            }else {
                builder.append(code);
            }
        }
        builder.append(")");
        return builder.toString();

    }
}
