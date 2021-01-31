package org.pecker.proxy.utils.beanutils;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.pecker.proxy.reflect.InvincibleMethod;

import java.lang.reflect.Method;

public class FastField {
    private InvincibleMethod fieldGetMethod;

    private InvincibleMethod fieldSetMethod;

    private Class fieldClass;

    private String fieldName;

    public FastField(Class fieldType, String fieldName, InvincibleMethod fieldGetMethod, InvincibleMethod fieldSetMethod) {
        this.fieldClass = fieldType;
        this.fieldName = fieldName;
        this.fieldGetMethod = fieldGetMethod;
        this.fieldSetMethod = fieldSetMethod;
    }

    public Object getValue(Object bean) {
        try {
            return fieldGetMethod.invoke(bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(Object bean, Object value) {
        try {
            fieldSetMethod.invoke(bean, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        return fieldGetMethod.getName();
    }

    public String getFieldSetMethodName() {
        return fieldSetMethod.getName();
    }
}
