package com.example.myapplication;

public class Post {
    private String content; //1
    private int likes; //1
    private int comments; //1
    private int shares; //1

    public Post(String content, int likes, int comments, int shares) { //2
        this.content = content; //2
        this.likes = likes; //2
        this.comments = comments; //2
        this.shares = shares; //2
    }

    public String getContent() { //3
        return content;
    }

    public int getLikes() { //3
        return likes;
    }

    public int getComments() { //3
        return comments;
    }

    public int getShares() { //3
        return shares;
    }
}
