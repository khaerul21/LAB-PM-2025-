package com.example.tuprak.models;

public class Story {
    private String username;
    private int profileImage;
    private boolean hasStory;
    private boolean isUserStory;
    private boolean isLive;
    private boolean isViewed;
    private String customProfileImageUri = null;

    public Story(String username, int profileImage, boolean hasStory, boolean isUserStory, boolean isLive) {
        this.username = username;
        this.profileImage = profileImage;
        this.hasStory = hasStory;
        this.isUserStory = isUserStory;
        this.isLive = isLive;
        this.isViewed = false; 
    }

    public String getUsername() {
        return username;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public boolean hasStory() {
        return hasStory;
    }

    public boolean isUserStory() {
        return isUserStory;
    }

    public boolean isLive() {
        return isLive;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public void setCustomProfileImageUri(String uri) {
        this.customProfileImageUri = uri;
    }

    public String getCustomProfileImageUri() {
        return customProfileImageUri;
    }

    public boolean hasCustomProfileImage() {
        return customProfileImageUri != null && !customProfileImageUri.isEmpty();
    }
}