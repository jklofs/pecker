package org.pecker.proxy.utils.beanutils;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class BeanCopyUtils {
    private static final ConcurrentMap<Integer, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class, List<FastField>> CLASS_FAST_FIELD_MAP = new ConcurrentHashMap<>();

    private BeanCopyUtils() {
    }

    public static <S, T> T copyByClass(S source, Class<T> targetClass, Converter converter) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            return copyByBean(source, target, converter);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("错误：{}", e);
        }
        return null;
    }

    public static <S, T> T copyByBean(S source, T target, Converter converter) {
        BeanCopier beanCopier = getBeanCopier(source, target, converter);
        beanCopier.copy(source, target, converter);
        return target;
    }

    private static <S, T> int getKey(S source, T target, Converter converter) {
        return source.getClass().hashCode() + target.getClass().hashCode() + (converter == null ? 0 : converter.hashCode());
    }

    private static <S, T> BeanCopier getBeanCopier(S source, T target, Converter converter) {
        int key = getKey(source, target, converter);
        BeanCopier beanCopier = BEAN_COPIER_MAP.get(key);
        if (beanCopier == null) {
            synchronized (BEAN_COPIER_MAP) {
                beanCopier = BEAN_COPIER_MAP.get(key);
                if (beanCopier == null) {
                    beanCopier = BeanCopier.create(source.getClass(), target.getClass(), converter != null);
                    BEAN_COPIER_MAP.put(key, beanCopier);
                }
            }
        }
        return beanCopier;
    }

    public static <T> T mergeBean(T sourceOne ,T sourceTwo){
        List<FastField> classFastFieldList = CLASS_FAST_FIELD_MAP.computeIfAbsent(sourceOne.getClass(),(tClass)->ReflectUtils.getFieldMethods(tClass));
        T result = null;
        try {
            result = (T) sourceOne.getClass().newInstance();
        } catch (InstantiationException |IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (FastField fastField : classFastFieldList ){
            Object value = fastField.getValue(sourceOne);
            if (value == null){
                value = fastField.getValue(sourceTwo);
            }
            if (value == null){
                continue;
            }

            if (value instanceof Number || value instanceof CharSequence || value instanceof Serializable){
                fastField.setValue(result,value);
            }else if (value instanceof Collection){
                Collection resultValue = null;
                try {
                    resultValue = (Collection) value.getClass().newInstance();
                } catch (InstantiationException |IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                Collection sourceOneValue = (Collection) fastField.getValue(sourceOne);
                Collection sourceTwoValue = (Collection) fastField.getValue(sourceOne);
                Iterator sourceOneValueIterator = sourceOneValue.iterator();
                Iterator sourceTwoValueIterator = sourceTwoValue.iterator();
                while (sourceOneValueIterator.hasNext() || sourceTwoValueIterator.hasNext()){
                    Object sourceOneItem = null;
                    if (sourceOneValueIterator.hasNext()){
                        sourceOneItem = sourceOneValueIterator.next();
                    }
                    Object sourceTwoItem = null;
                    if (sourceOneValueIterator.hasNext()){
                        sourceTwoItem = sourceTwoValueIterator.next();
                    }
                    resultValue.add(mergeBean(sourceOneItem,sourceTwoItem));
                }
                fastField.setValue(result,resultValue);
            }else if (value instanceof Map){
                Map resultValue = null;
                try {
                    resultValue = (Map) value.getClass().newInstance();
                } catch (InstantiationException |IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                Map sourceOneValue = (Map) fastField.getValue(sourceOne);
                Map sourceTwoValue = (Map) fastField.getValue(sourceOne);
                Set<String> sourceOneValueKeySet = sourceOneValue.keySet();
                Set<String> sourceTwoValueKeySet  = sourceTwoValue.keySet();
                Set<String> sourceNameSet = new HashSet<>();
                sourceNameSet.addAll(sourceOneValueKeySet);
                sourceNameSet.addAll(sourceTwoValueKeySet);
                for (String key : sourceNameSet){
                    Object sourceOneItem = sourceOneValue.get(key);
                    Object sourceTwoItem = sourceOneValue.get(key);
                    Object resultItem = mergeBean(sourceOneItem,sourceTwoItem);
                    resultValue.put(key,resultItem);
                }
                fastField.setValue(result,resultValue);
            }else  {
                Object resultValue = mergeBean(fastField.getValue(sourceOne),fastField.getValue(sourceTwo));
                fastField.setValue(result,resultValue);
            }

        }
        return result;
    }
}