package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

public class BooleanRule implements ValidationRule {

    @Override
    public boolean check(Object value, String... params) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return true;
        }
        if (value instanceof String str) {
            String val = str.trim().toLowerCase();
            return val.equals("true") || val.equals("false") || val.equals("1") || val.equals("0");
        }
        if (value instanceof Number num) {
            return num.intValue() == 0 || num.intValue() == 1;
        }
        return false;
    }

    @Override
    public String message(String fieldName, String... params) {
        String label = (fieldName != null && !fieldName.isBlank()) ? fieldName : "This field";
        return label + " must be true or false.";
    }

}
