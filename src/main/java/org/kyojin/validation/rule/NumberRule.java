package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

public class NumberRule implements ValidationRule {

    @Override
    public boolean check(Object value, String... params) {
        if (value == null) {
            return false;
        }

        if (value instanceof Number) {
            return true;
        }

        if (value instanceof String str) {
            str = str.trim();
            if (str.isEmpty()) {
                return false;
            }

            try {
                double num = Double.parseDouble(str);
                return !Double.isNaN(num) && !Double.isInfinite(num);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public String message(String fieldName, String... params) {
        String label = (fieldName != null && !fieldName.isBlank()) ? fieldName : "This field";
        return label + " must be a valid number.";
    }
}
