package com.okatu.rgan.common.model;

import java.util.ArrayList;
import java.util.List;

public class MethodArgumentNotValidErrorResult {
    private String message;

    private List<FieldError> fieldErrors = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String fieldName, String message){
        fieldErrors.add(new FieldError(fieldName, message));
    }

    public static class FieldError{
        public String fieldName;

        public String message;

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }

        public FieldError(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }
    }
}
