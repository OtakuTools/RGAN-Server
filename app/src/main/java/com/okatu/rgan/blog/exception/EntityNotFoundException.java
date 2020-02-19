package com.okatu.rgan.blog.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, Long id) {
        super("Could not find " + type + ", id: " + id);
    }

    public EntityNotFoundException(String message){
        super(message);
    }
}
