package org.pecker.common.beanutils;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReflectUtils {
    protected static final MultiKeyMap<String,FastField> FIELD_ACCESS_MAP = new MultiKeyMap<>();

    protected static final ConcurrentMap<Class,MethodAccess> METHOD_ACCESS_MAP = new ConcurrentHashMap<>();

    /**
     * 获取field的值
     *
     * @param beanClass
     * @param bean
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Class beanClass,Object bean,String fieldName){
        FastField methodAccess = getFieldMethodAccess(beanClass,fieldName);
        if (null == methodAccess){
            return null;
        }
        return methodAccess.getValue(bean);
    }

    /**
     * 设置field的值
     *
     * @param beanClass
     * @param bean
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldValue(Class beanClass,Object bean,String fieldName,Object fieldValue){
        FastField methodAccess = getFieldMethodAccess(beanClass,fieldName);
        if (null == methodAccess){
            return;
        }
        methodAccess.setValue(bean,fieldValue);
        return;
    }

    /**
     * 执行方法
     *
     * @param beanClass
     * @param bean
     * @param methodName
     * @param args
     * @return
     */
    public static Object invokeMethod(Class beanClass,Object bean,String methodName,Object... args){
        MethodAccess methodAccess = MethodAccess.get(beanClass);
        if (methodAccess == null){
            methodAccess = MethodAccess.get(beanClass);
            METHOD_ACCESS_MAP.put(beanClass,methodAccess);
        }
        return methodAccess.invoke(bean,methodName,args);
    }

    /**
     * 获取field的方法
     *
     * @param beanClass
     * @param fieldName
     * @return
     */
    public static FastField getFieldMethodAccess(Class beanClass,String fieldName){
        FastField fieldMethod = FIELD_ACCESS_MAP.get(beanClass.getName(),fieldName);
        if (fieldMethod == null){
            MethodAccess methodAccess = MethodAccess.get(beanClass);
            createdClassPropertyDescriptors(beanClass,methodAccess);
            fieldMethod = FIELD_ACCESS_MAP.get(beanClass.getName(),fieldName);
        }
        return fieldMethod;
    }

    /**
     * 获取类对象的所有field
     *
     * @param beanClass
     * @return
     */
    public static List<FastField> getFieldMethods(Class beanClass){
        MethodAccess methodAccess = MethodAccess.get(beanClass);
        return createdClassPropertyDescriptors(beanClass,methodAccess);
    }

    /**
     *
     * @param beanClass
     * @param methodAccess
     */
    private static List<FastField> createdClassPropertyDescriptors(Class beanClass,MethodAccess methodAccess){
        String beanName = beanClass.getName();
        List<FastField> fieldMethods = new ArrayList<>();
        PropertyDescriptor[] propertyDescriptors = org.springframework.cglib.core.ReflectUtils.getBeanProperties(beanClass);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
            FastField fieldMethod = new FastField(methodAccess,propertyDescriptor.getPropertyType(),propertyDescriptor.getName(),propertyDescriptor
                    .getReadMethod(),propertyDescriptor.getWriteMethod());
            FIELD_ACCESS_MAP.put(beanName,propertyDescriptor.getName(),fieldMethod);
            fieldMethods.add(fieldMethod);
        }
        return fieldMethods;
    }
}
