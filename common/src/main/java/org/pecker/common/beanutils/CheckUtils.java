package org.pecker.common.beanutils;

import com.hzgroup.engine.common.customexception.BusinessException;
import com.hzgroup.engine.common.enumeration.ResultCodeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CheckUtils {

    private static final ConcurrentMap<Class, List<FastField>> CLASS_FAST_FIELD_MAP = new ConcurrentHashMap<>();

    private CheckUtils(){
    }

    /**
     * 校验值是否为存在null
     *
     * @param source
     * @param shouldCheckFieldSet
     * @param <T>
     */
   public static <T> void checkHasAnyNull(T source, Set<String> shouldCheckFieldSet){
        if (CollectionUtils.isEmpty(shouldCheckFieldSet)){
            return;
        }
        List<FastField> classFastFieldList = CLASS_FAST_FIELD_MAP.computeIfAbsent(source.getClass(),(tClass)->ReflectUtils.getFieldMethods(tClass));
        for (FastField fastField : classFastFieldList){
            if (!shouldCheckFieldSet.contains(fastField.getFieldName())){
                continue;
            }
            Object value = fastField.getValue(source);
            if (value == null){
                throw new BusinessException(ResultCodeEnum.PARAMETER_NOT,fastField.getFieldName());
            }
        }
    }

    /**
     * 校验值是否为存在null
     *
     * @param source
     * @param shouldCheckFieldSet
     * @param <T>
     */
    public static <T> void checkHasAllNull(T source, Set<String> shouldCheckFieldSet){
        if (CollectionUtils.isEmpty(shouldCheckFieldSet)){
            return;
        }
        List<FastField> classFastFieldList = CLASS_FAST_FIELD_MAP.computeIfAbsent(source.getClass(),(tClass)->ReflectUtils.getFieldMethods(tClass));
        int nullNum = 0;
        for (FastField fastField : classFastFieldList){
            if (!shouldCheckFieldSet.contains(fastField.getFieldName())){
                continue;
            }
            Object value = fastField.getValue(source);
            if (value == null){
                nullNum++;
            }
        }
        if (nullNum<shouldCheckFieldSet.size()){
            throw new BusinessException(ResultCodeEnum.PARAMETER_NOT,shouldCheckFieldSet);
        }
    }

    /**
     * 校验值是否为null
     *
     * @param source
     * @param shouldCheckFieldSet
     * @param <T>
     */
    public static <T> void checkHasAnyBlank(T source, Set<String> shouldCheckFieldSet){
        if (CollectionUtils.isEmpty(shouldCheckFieldSet)){
            return;
        }
        List<FastField> classFastFieldList = CLASS_FAST_FIELD_MAP.computeIfAbsent(source.getClass(),(tClass)->ReflectUtils.getFieldMethods(tClass));
        for (FastField fastField : classFastFieldList){
            if (!shouldCheckFieldSet.contains(fastField.getFieldName())){
                continue;
            }
            Object value = fastField.getValue(source);
            if (value == null){
                throw new BusinessException(ResultCodeEnum.PARAMETER_NOT,fastField.getFieldName());
            }
            if (!value.getClass().isAssignableFrom(CharSequence.class)){
                continue;
            }
            if (StringUtils.isBlank((CharSequence) value)){
                throw new BusinessException(ResultCodeEnum.PARAMETER_NOT,fastField.getFieldName());
            }
        }
    }

    /**
     * 校验值是否为null
     *
     * @param source
     * @param shouldCheckFieldSet
     * @param <T>
     */
    public static <T> void checkHasAllBlank(T source, Set<String> shouldCheckFieldSet){
        if (CollectionUtils.isEmpty(shouldCheckFieldSet)){
            return;
        }
        int blankNum = 0;
        List<FastField> classFastFieldList = CLASS_FAST_FIELD_MAP.computeIfAbsent(source.getClass(),(tClass)->ReflectUtils.getFieldMethods(tClass));
        for (FastField fastField : classFastFieldList){
            if (!shouldCheckFieldSet.contains(fastField.getFieldName())){
                continue;
            }
            Object value = fastField.getValue(source);
            if (value == null){
                blankNum++;
            }
            if (!value.getClass().isAssignableFrom(CharSequence.class)){
                blankNum++;
                continue;
            }
            if (StringUtils.isBlank((CharSequence) value)){
                blankNum++;
            }
        }
        if (blankNum<shouldCheckFieldSet.size()){
            throw new BusinessException(ResultCodeEnum.PARAMETER_NOT,shouldCheckFieldSet);
        }
    }

}
