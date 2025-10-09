package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class MinRule implements ValidationRule {

    @Override
    public boolean check(Object value, String... params) {
        if (value == null || params.length == 0) {
            return false;
        }

        int min;
        try {
            min = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (value instanceof Number num) {
            return num.doubleValue() >= min;
        }

        if (value instanceof String str) {
            return str.trim().length() >= min;
        }

        if (value instanceof Collection<?> col) {
            return col.size() >= min;
        }

        if (value instanceof Map<?, ?> map) {
            return map.size() >= min;
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) >= min;
        }

        return false;
    }

    @Override
    public String message(String fieldName, String... params) {
        String label = (fieldName != null && !fieldName.isBlank()) ? fieldName : "This field";
        String minValue = (params.length > 0 ? params[0] : "?");
        return label + " must be at least " + minValue + " characters.";
    }

}
