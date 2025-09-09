package com.example.instagramclone.model;

public class Story {
    private String id;
    private String imageUrl;
    private String title;
    private String userId;

    public Story(String id, String imageUrl, String title, String userId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // ... tambahkan getter dan setter lainnya
}
