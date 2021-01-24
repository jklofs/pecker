package org.pecker.common.map;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
public class DefaultKeyHashMap<k,v> implements Map<k,v> {

    private Map<k,v> map;

    private k defaultKey;

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public v get(Object key) {
        v result = map.get(key);
        if (result == null){
            result = map.get(defaultKey);
        }
        return result;
    }

    @Override
    public v put(k key, v value) {
        return map.put(key, value);
    }

    @Override
    public v remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends k, ? extends v> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<k> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<v> values() {
        return map.values();
    }

    @Override
    public Set<Entry<k, v>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public v getOrDefault(Object key, v defaultValue) {
        v value;
        return (((value = get(key)) != null) || containsKey(key))
                ? value
                : (((value = get(defaultKey)) != null) || containsKey(defaultKey))
                ? value : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super k, ? super v> action) {
        map.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super k, ? super v, ? extends v> function) {
        map.replaceAll(function);
    }

    @Override
    public v putIfAbsent(k key, v value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public boolean replace(k key, v oldValue, v newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public v replace(k key, v value) {
        return map.replace(key, value);
    }

    @Override
    public v computeIfAbsent(k key, Function<? super k, ? extends v> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public v computeIfPresent(k key, BiFunction<? super k, ? super v, ? extends v> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public v compute(k key, BiFunction<? super k, ? super v, ? extends v> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    @Override
    public v merge(k key, v value, BiFunction<? super v, ? super v, ? extends v> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }

}
