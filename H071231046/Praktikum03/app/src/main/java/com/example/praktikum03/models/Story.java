package com.example.praktikum03.models;

import java.util.Date;
import java.util.List;

public class Story {
    private int id;
    private String title;
    private int thumbnailResourceId;
    private List<Integer> imageResourceIds;
    private Date storyDate;

    public Story(int id, String title, int thumbnailResourceId, List<Integer> imageResourceIds, Date storyDate) {
        this.id = id;
        this.title = title;
        this.thumbnailResourceId = thumbnailResourceId;
        this.imageResourceIds = imageResourceIds;
        this.storyDate = storyDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbnailResourceId() {
        return thumbnailResourceId;
    }

    public void setThumbnailResourceId(int thumbnailResourceId) {
        this.thumbnailResourceId = thumbnailResourceId;
    }

    public List<Integer> getImageResourceIds() {
        return imageResourceIds;
    }

    public void setImageResourceIds(List<Integer> imageResourceIds) {
        this.imageResourceIds = imageResourceIds;
    }

    public Date getStoryDate() {
        return storyDate;
    }

    public void setStoryDate(Date storyDate) {
        this.storyDate = storyDate;
    }
}