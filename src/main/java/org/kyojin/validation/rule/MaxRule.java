package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class MaxRule implements ValidationRule {

    @Override
    public boolean check(Object value, String... params) {
        if (value == null || params.length == 0) {
            return false;
        }

        int max;
        try {
            max = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (value instanceof Number num) {
            return num.doubleValue() <= max;
        }

        if (value instanceof String str) {
            return str.trim().length() <= max;
        }

        if (value instanceof Collection<?> col) {
            return col.size() <= max;
        }

        if (value instanceof Map<?, ?> map) {
            return map.size() <= max;
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) <= max;
        }

        return false;
    }

    @Override
    public String message(String fieldName, String... params) {
        String label = (fieldName != null && !fieldName.isBlank()) ? fieldName : "This field";
        String maxValue = (params.length > 0 ? params[0] : "?");
        return label + " must not exceed " + maxValue + " characters.";
    }
}
