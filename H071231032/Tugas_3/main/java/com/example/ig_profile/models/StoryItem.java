package com.example.ig_profile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class StoryItem implements Parcelable {
    private String cover;
    private String title;
    private List<Integer> imageList;

    public StoryItem(String cover, String title, List<Integer> imageList) {
        this.cover = cover;
        this.title = title;
        this.imageList = imageList;
    }

    protected StoryItem(Parcel in) {
        cover = in.readString();
        title = in.readString();
        imageList = new ArrayList<>();
        in.readList(imageList, Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cover);
        dest.writeString(title);
        dest.writeList(imageList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StoryItem> CREATOR = new Creator<StoryItem>() {
        @Override
        public StoryItem createFromParcel(Parcel in) {
            return new StoryItem(in);
        }

        @Override
        public StoryItem[] newArray(int size) {
            return new StoryItem[size];
        }
    };

    public String getCover() { return cover; }
    public String getTitle() { return title; }
    public List<Integer> getImageList() { return imageList; }
}