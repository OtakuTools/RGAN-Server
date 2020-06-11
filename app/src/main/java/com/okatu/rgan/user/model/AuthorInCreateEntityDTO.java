package com.okatu.rgan.user.model;

public class AuthorInCreateEntityDTO {
    private String username;

    private String profilePicturePath;

    public AuthorInCreateEntityDTO() {
    }

    public AuthorInCreateEntityDTO(String username, String profilePicturePath) {
        this.username = username;
        this.profilePicturePath = profilePicturePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}
