package com.example.tuprak.models;

public class Post {
    private String username;
    private int profileImage; // Resource ID for profile image
    private int postImage;    // Resource ID for post image
    private String caption;
    private int likes;
    private int comments;
    private String timePosted;
    
    public Post(String username, int profileImage, int postImage, String caption, int likes, int comments, String timePosted) {
        this.username = username;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.caption = caption;
        this.likes = likes;
        this.comments = comments;
        this.timePosted = timePosted;
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
    
    public int getPostImage() {
        return postImage;
    }
    
    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public int getLikes() {
        return likes;
    }
    
    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    public int getComments() {
        return comments;
    }
    
    public void setComments(int comments) {
        this.comments = comments;
    }
    
    public String getTimePosted() {
        return timePosted;
    }
    
    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }
}