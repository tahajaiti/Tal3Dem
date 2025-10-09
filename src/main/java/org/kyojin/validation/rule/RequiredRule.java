package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class RequiredRule implements ValidationRule {

    @Override
    public boolean check(Object value, String... params) {
        if (value == null) {
            return false;
        }

        if (value instanceof String str) {
            return !str.trim().isEmpty();
        }

        if (value instanceof Collection<?> col) {
            return !col.isEmpty();
        }

        if (value instanceof Map<?, ?> map) {
            return !map.isEmpty();
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }

        return true;
    }

    @Override
    public String message(String field, String... params) {
        return (field != null && !field.isBlank() ? field : "This field") + " is required.";
    }
}
