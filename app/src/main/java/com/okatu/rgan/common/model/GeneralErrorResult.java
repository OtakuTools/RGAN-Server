package com.okatu.rgan.common.model;

public class GeneralErrorResult {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GeneralErrorResult(String message) {
        this.message = message;
    }

    public GeneralErrorResult() {
    }
}
