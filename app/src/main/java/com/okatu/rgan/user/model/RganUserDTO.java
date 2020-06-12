package com.okatu.rgan.user.model;

import java.time.LocalDateTime;

public class RganUserDTO {
    private Long id;

    private String username;

    private LocalDateTime createdTime;

    private String profilePicturePath;


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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public static RganUserDTO convertFrom(RganUser user){
        RganUserDTO rganUserDTO = new RganUserDTO();
        rganUserDTO.setId(user.getId());
        rganUserDTO.setUsername(user.getUsername());
        rganUserDTO.setCreatedTime(user.getCreatedTime());
        rganUserDTO.setProfilePicturePath(user.getProfilePicturePath());
        return rganUserDTO;
    }
}
