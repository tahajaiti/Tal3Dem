package org.kyojin.validation.rule;

import org.kyojin.validation.ValidationRule;

public class EmailRule implements ValidationRule {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public boolean check(Object value, String... params) {
        if (value == null) {
            return false;
        }

        String email = value.toString().trim();
        if (email.isEmpty()) {
            return false;
        }

        return email.matches(EMAIL_REGEX);
    }

    @Override
    public String message(String fieldName, String... params) {
        String label = (fieldName != null && !fieldName.isBlank()) ? fieldName : "This field";
        return label + " must be a valid email address.";
    }
}
