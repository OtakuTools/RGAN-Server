package com.okatu.rgan.common.exception;

public class VerificationEmailUnavailableException extends RuntimeException {
    public VerificationEmailUnavailableException(String email) {
        super("Email address: " + email + " had been verified");
    }
}
