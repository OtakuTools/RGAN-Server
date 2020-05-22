package com.okatu.rgan.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String type, Long id) {
        super("Could not find " + type + ", id: " + id);
    }

    public ResourceNotFoundException(String type, String name){
        super("Could not find " + type + ", name: " + name);
    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}
