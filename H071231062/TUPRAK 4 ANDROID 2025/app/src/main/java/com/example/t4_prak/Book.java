package com.example.t4_prak;

public class Book {
    private String title;
    private String author;
    private int year;
    private String blurb;
    private int coverImageResId;
    private String coverImageUri;
    private boolean isLiked;
    private String genre;
    private float rating;

    public Book(String title, String author, int year, String blurb, int coverImageResId, boolean isLiked, String genre, float rating) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.blurb = blurb;
        this.coverImageResId = coverImageResId;
        this.isLiked = isLiked;
        this.genre = genre;
        this.rating = rating;
    }
    public Book(String title, String author, int year, String blurb, String coverImageUri, boolean isLiked, String genre, float rating) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.blurb = blurb;
        this.coverImageUri = coverImageUri;
        this.isLiked = isLiked;
        this.genre = genre;
        this.rating = rating;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public int getCoverImageResId() {
        return coverImageResId;
    }

    public void setCoverImageResId(int coverImageResId) {
        this.coverImageResId = coverImageResId;
    }

    public String getCoverImageUri() {
        return coverImageUri;
    }

    public void setCoverImageUri(String coverImageUri) {
        this.coverImageUri = coverImageUri;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}