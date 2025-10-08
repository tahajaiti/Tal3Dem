package org.kyojin.core.exception;

public class InjectionFailedException extends RuntimeException {
    public InjectionFailedException(String message) {
        super(message);
    }
}
