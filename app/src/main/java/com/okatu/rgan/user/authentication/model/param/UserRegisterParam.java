package com.okatu.rgan.user.authentication.model.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegisterParam {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRegisterParam{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
