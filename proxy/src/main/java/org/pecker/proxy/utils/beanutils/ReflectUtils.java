package org.pecker.proxy.utils.beanutils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.pecker.proxy.reflect.InvincibleMethod;
import org.pecker.proxy.reflect.InvincibleMethodFactory;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtils {
    protected static final MultiKeyMap<String,FastField> FIELD_ACCESS_MAP = new MultiKeyMap<>();

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
        List<InvincibleMethod> invincibleMethodList = InvincibleMethodFactory.create(beanClass);
        if (CollectionUtils.isEmpty(invincibleMethodList)){
            throw new RuntimeException(new NoSuchMethodException());
        }
        Class[] argsTypes;
        if (args!=null) {
            argsTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    argsTypes[i] = null;
                } else {
                    argsTypes[i] = args[i].getClass();
                }
            }
        }else {
            argsTypes = new Class[0];
        }
        InvincibleMethod invincibleMethod = invincibleMethodList.stream()
                .filter(item->methodName.equals(item.getName())&& Arrays.equals(argsTypes,item.getParameterTypes())).findFirst().orElse(null);
        if (invincibleMethod == null){
            throw new RuntimeException(new NoSuchMethodException());
        }
        try {
            return invincibleMethod.invoke(bean,methodName,args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            createdClassPropertyDescriptors(beanClass);
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
        return createdClassPropertyDescriptors(beanClass);
    }

    /**
     *
     * @param beanClass
     */
    private static List<FastField> createdClassPropertyDescriptors(Class beanClass){
        String beanName = beanClass.getName();
        List<InvincibleMethod> invincibleMethodList = InvincibleMethodFactory.create(beanClass);
        List<FastField> fieldMethods = new ArrayList<>();
        PropertyDescriptor[] propertyDescriptors = net.sf.cglib.core.ReflectUtils.getBeanProperties(beanClass);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
            InvincibleMethod fieldGetMethod = null;
            if (propertyDescriptor.getPropertyType().equals(boolean.class)){
                fieldGetMethod = invincibleMethodList.stream().filter(invincibleMethod -> invincibleMethod.getName()
                        .equals("is"+ StringUtils.capitalize(propertyDescriptor.getName()))).findFirst().orElse(null);
            }else {
                fieldGetMethod = invincibleMethodList.stream().filter(invincibleMethod -> invincibleMethod.getName()
                        .equals("get"+ StringUtils.capitalize(propertyDescriptor.getName()))).findFirst().orElse(null);
            }
            InvincibleMethod fieldSetMethod = invincibleMethodList.stream().filter(invincibleMethod -> invincibleMethod.getName()
                    .equals("set"+ StringUtils.capitalize(propertyDescriptor.getName()))).findFirst().orElse(null);
            FastField fieldMethod = new FastField(propertyDescriptor.getPropertyType(),propertyDescriptor.getName(),fieldGetMethod,fieldSetMethod);
            FIELD_ACCESS_MAP.put(beanName,propertyDescriptor.getName(),fieldMethod);
            fieldMethods.add(fieldMethod);
        }
        return fieldMethods;
    }
}
