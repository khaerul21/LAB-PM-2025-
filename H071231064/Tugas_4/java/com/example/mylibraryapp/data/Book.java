package com.example.mylibraryapp.data;

import java.io.Serializable;

public class Book implements Serializable {

    private String imageUrl;     // Image URL (URI as String)
    private String title;        // Book title
    private String author;       // Author of the book
    private int year;            // Year of publication
    private String description;  // Description of the book
    private boolean isLiked;     // Status whether the book is liked or not
    private int stock;           // The stock of the book

    // Constructor
    public Book(String imageUrl, String title, String author, int year, String description, boolean isLiked, int stock) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.author = author;
        this.year = year;
        this.description = description;
        this.isLiked = isLiked;
        this.stock = stock;
    }

    // Getter methods
    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    // Getters and setters for 'isLiked' status
    public boolean getBookStatus() {
        return isLiked;
    }

    public void setBookStatus(boolean isLiked) {
        this.isLiked = isLiked;
    }

    // Setter methods
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
