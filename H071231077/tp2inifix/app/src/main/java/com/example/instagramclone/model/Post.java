package com.example.instagramclone.model;

public class Post {
    private String id;
    private String imageUrl;
    private String caption;
    private String userId;
    private String userProfilePic;
    private String username;
    private int likes;
    private int comments;

    public Post(String id, String imageUrl, String caption, String userId,
                String userProfilePic, String username) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.userId = userId;
        this.userProfilePic = userProfilePic;
        this.username = username;
        this.likes = 0;
        this.comments = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    // ... tambahkan getter dan setter lainnya
}
