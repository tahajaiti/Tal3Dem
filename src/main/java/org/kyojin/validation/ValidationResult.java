package org.kyojin.validation;

public class ValidationResult {
    private final boolean passed;
    private final String message;

    private ValidationResult(boolean passed, String message) {
        this.passed = passed;
        this.message = message;
    }

    public boolean isPassed() {
        return passed;
    }

    public String message() {
        return message;
    }

    public static ValidationResult pass() {
        return new ValidationResult(true, "");
    }

    public static ValidationResult fail(String message) {
        return new ValidationResult(false, message);
    }
}
