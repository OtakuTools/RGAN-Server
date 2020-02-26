package com.okatu.rgan.user.model;

import javax.persistence.*;
import java.time.LocalDateTime;

// about the state transition
// see: https://www.yuque.com/fugi8p/vawoon/ghaqgf
@Entity
public class UserVerification {
    public static final int CREATED = 1;

    public static final int EXPIRED = 2;

    public static final int VERIFIED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String token;

//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdTime;

    private Integer status = CREATED;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
