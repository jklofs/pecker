package org.pecker.common.beanutils;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Method;

public class FastField {
    private MethodAccess methodAccess;

    private Class fieldClass;

    private String fieldName;

    private String fieldGetMethodName;

    private String fieldSetMethodName;

    private int fieldGetIndex;

    private int fieldSetIndex;

    public FastField(MethodAccess methodAccess, Class fieldType, String fieldName, Method fieldGetMethod, Method fieldSetMethod) {
        this.methodAccess = methodAccess;
        this.fieldClass = fieldType;
        this.fieldName = fieldName;
        if (fieldGetMethod != null) {
            this.fieldGetMethodName = fieldGetMethod.getName();
            this.fieldGetIndex = methodAccess.getIndex(fieldGetMethodName);
        }
        if (fieldSetMethod != null) {
            this.fieldSetMethodName = fieldSetMethod.getName();
            this.fieldSetIndex = methodAccess.getIndex(fieldSetMethodName);
        }
    }

    public Object getValue(Object bean) {
        return methodAccess.invoke(bean, fieldGetIndex);
    }

    public void setValue(Object bean, Object value) {
        methodAccess.invoke(bean, fieldSetIndex, value);
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldGetMethodName() {
        return fieldGetMethodName;
    }

    public void setFieldGetMethodName(String fieldGetMethodName) {
        this.fieldGetMethodName = fieldGetMethodName;
    }

    public String getFieldSetMethodName() {
        return fieldSetMethodName;
    }

    public void setFieldSetMethodName(String fieldSetMethodName) {
        this.fieldSetMethodName = fieldSetMethodName;
    }
}
