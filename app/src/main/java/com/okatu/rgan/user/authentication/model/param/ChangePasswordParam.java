package com.okatu.rgan.user.authentication.model.param;

import javax.validation.constraints.NotBlank;

public class ChangePasswordParam {
    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
