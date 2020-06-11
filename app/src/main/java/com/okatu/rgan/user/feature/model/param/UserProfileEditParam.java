package com.okatu.rgan.user.feature.model.param;

public class UserProfileEditParam {
    private String profilePicturePath;

    private String description;

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
