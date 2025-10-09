package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

public class StringRule implements ValidationRule {

    private static final String STRING_REGEX = "^[A-Za-z]+$";

    @Override
    public boolean check(Object value, String... params) {
        if (value == null) {
            return false;
        }

        String str = value.toString().trim();
        if (str.isEmpty()) {
            return false;
        }

        return str.matches(STRING_REGEX);
    }

    @Override
    public String message(String fieldName, String... params) {
        String label = (fieldName != null && !fieldName.isBlank()) ? fieldName : "This field";
        return label + " must contain only alphabetic characters.";
    }
}
