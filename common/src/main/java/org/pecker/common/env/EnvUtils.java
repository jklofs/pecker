package org.pecker.common.env;

import java.util.HashMap;
import java.util.Map;

public class EnvUtils {

    private EnvUtils() {
    }

    public static String getProperty(String key) {
        String result = System.getProperty(key);
        if (result != null) {
            return result;
        }
        return System.getenv(key);
    }

    public static String getPropertyDefault(String key ,String defaultValue){
        String result = getProperty(key);
        if (result != null){
            return result;
        }
        return defaultValue;
    }

    public static void setProperty(String key, String value) {
        System.setProperty(key, value);
    }

    public static void setProperties(Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    public static Map<String, String> getProperties() {
        Map<String, String> result = new HashMap<>(System.getenv());
        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            result.put((String) entry.getKey(), (String) entry.getValue());
        }
        return result;
    }
}
