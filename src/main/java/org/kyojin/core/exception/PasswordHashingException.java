package org.kyojin.core.exception;

public class PasswordHashingException extends RuntimeException {
    public PasswordHashingException(String message) {
        super(message);
    }
}
