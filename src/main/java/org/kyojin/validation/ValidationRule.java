package org.kyojin.validation;

public interface ValidationRule {
    boolean check(Object value, String... params);

    String message(String field, String... params);
}
