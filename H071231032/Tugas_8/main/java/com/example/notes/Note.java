package com.example.notes;

public class Note {
    private int id;
    private String title;
    private String description;
    private long createdAt;
    private long updatedAt;
    private boolean titleChangedInLastUpdate;
    private boolean descriptionChangedInLastUpdate;

    // Konstruktor untuk cursor
    public Note() {
    }

    public Note(int id, String title, String description, long createdAt, long updatedAt,
                boolean titleChangedInLastUpdate, boolean descriptionChangedInLastUpdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.titleChangedInLastUpdate = titleChangedInLastUpdate;
        this.descriptionChangedInLastUpdate = descriptionChangedInLastUpdate;
    }

    // Getter dan Setter
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean wasTitleChangedInLastUpdate() { return titleChangedInLastUpdate; }
    public void setTitleChangedInLastUpdate(boolean titleChangedInLastUpdate) {
        this.titleChangedInLastUpdate = titleChangedInLastUpdate;
    }
    public boolean wasDescriptionChangedInLastUpdate() { return descriptionChangedInLastUpdate; }
    public void setDescriptionChangedInLastUpdate(boolean descriptionChangedInLastUpdate) {
        this.descriptionChangedInLastUpdate = descriptionChangedInLastUpdate;
    }
}