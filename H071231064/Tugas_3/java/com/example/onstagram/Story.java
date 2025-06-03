package com.example.onstagram;

public class Story {
    private String imageUrl;
    private String username;

    public Story(String imageUrl, String username) {
        this.imageUrl = imageUrl;
        this.username = username;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getUsername(){
        return  username;
    }
}

