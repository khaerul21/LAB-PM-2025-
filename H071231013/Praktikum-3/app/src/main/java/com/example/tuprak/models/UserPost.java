package com.example.tuprak.models;

public class UserPost {
    private String id;
    private String username;
    private String userId;   
    private String userProfileImageUri;
    private String postImageUri;
    private String caption;
    private int likesCount;
    private int commentsCount;
    private String timePosted;
    private String timestamp;

    public UserPost(String id, String username, String userId, String userProfileImageUri,
            String postImageUri, String caption, int likesCount,
            int commentsCount, String timePosted) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.userProfileImageUri = userProfileImageUri;
        this.postImageUri = postImageUri;
        this.caption = caption;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.timePosted = timePosted;
        this.timestamp = timePosted;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserProfileImageUri() {
        return userProfileImageUri;
    }

    public String getPostImageUri() {
        return postImageUri;
    }

    public String getCaption() {
        return caption;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public String getTimestamp() {
        return timestamp;
    }
}