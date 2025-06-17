package com.example.ig_profile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class HighlightItem implements Parcelable {
    private int coverResId;
    private String title;
    private List<Integer> imageList;

    public HighlightItem(int coverResId, String title, List<Integer> imageList) {
        this.coverResId = coverResId;
        this.title = title;
        this.imageList = imageList;
    }

    protected HighlightItem(Parcel in) {
        coverResId = in.readInt();
        title = in.readString();
        imageList = new ArrayList<>();
        in.readList(imageList, Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(coverResId);
        dest.writeString(title);
        dest.writeList(imageList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HighlightItem> CREATOR = new Creator<HighlightItem>() {
        @Override
        public HighlightItem createFromParcel(Parcel in) {
            return new HighlightItem(in);
        }

        @Override
        public HighlightItem[] newArray(int size) {
            return new HighlightItem[size];
        }
    };

    public int getCoverResId() { return coverResId; }
    public String getTitle() { return title; }
    public List<Integer> getImageList() { return imageList; }
}