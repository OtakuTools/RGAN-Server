package com.okatu.rgan.common.exception;

public class ResourceAccessDeniedException extends RuntimeException {
    public ResourceAccessDeniedException(String message) {
        super(message);
    }
}
