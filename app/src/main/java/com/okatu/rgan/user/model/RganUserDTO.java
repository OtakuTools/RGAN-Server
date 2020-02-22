package com.okatu.rgan.user.model;

import java.util.Date;

public class RganUserDTO {
    private Long id;

    private String username;

    private Date createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public static RganUserDTO convertFrom(RganUser user){
        RganUserDTO rganUserDTO = new RganUserDTO();
        rganUserDTO.setId(user.getId());
        rganUserDTO.setUsername(user.getUsername());
        rganUserDTO.setCreatedTime(user.getCreatedTime());
        return rganUserDTO;
    }
}
