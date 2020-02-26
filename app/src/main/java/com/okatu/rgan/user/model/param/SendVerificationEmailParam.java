package com.okatu.rgan.user.model.param;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SendVerificationEmailParam {
    @NotBlank
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
