package com.example.myapplication;

public class Post {
    private String content;
    private int commentCount;
    private int retweetCount;
    private int likeCount;

    public Post(String content, int commentCount, int retweetCount, int likeCount) {
        this.content = content;
        this.commentCount = commentCount;
        this.retweetCount = retweetCount;
        this.likeCount = likeCount;
    }

    // Getters
    public String getContent() { return content; }
    public int getCommentCount() { return commentCount; }
    public int getRetweetCount() { return retweetCount; }
    public int getLikeCount() { return likeCount; }
}
