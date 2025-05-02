package com.example.ig_profile.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class FeedItem implements Parcelable {
    private Object image;
    private String ownerFeeds, caption;
    private int likes;
    private int comments;
    private int shares;

    public FeedItem(Object image, String caption, int likes, int comments, int shares) {
        this.image = image;
        this.caption = caption;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
    }

    public FeedItem(Object image, String caption) {
        this.image = image;
        this.caption = caption;
        this.likes = 0;
        this.comments = 0;
        this.shares = 0;
    }

    protected FeedItem(Parcel in) {
        boolean isUri = in.readByte() != 0;
        if (isUri) {
            image = in.readParcelable(Uri.class.getClassLoader());
        } else {
            image = in.readInt();
        }
        ownerFeeds = in.readString();
        caption = in.readString();
        likes = in.readInt();
        comments = in.readInt();
        shares = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (image instanceof Uri) {
            dest.writeByte((byte) 1);
            dest.writeParcelable((Uri) image, flags);
        } else {
            dest.writeByte((byte) 0);
            dest.writeInt((Integer) image);
        }
        dest.writeString(ownerFeeds);
        dest.writeString(caption);
        dest.writeInt(likes);
        dest.writeInt(comments);
        dest.writeInt(shares);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FeedItem> CREATOR = new Creator<FeedItem>() {
        @Override
        public FeedItem createFromParcel(Parcel in) {
            return new FeedItem(in);
        }

        @Override
        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };

    public boolean isUri() { return image instanceof Uri; }
    public Uri getImageUri() { return (Uri) image; }
    public int getImageResId() {
        if (image instanceof Integer) {
            return (int) image;
        } else if (image instanceof Double) {
            return ((Double) image).intValue();
        } else {
            return 0;
        }
    }
    public String getOwnerFeeds() { return ownerFeeds; }
    public void setOwnerFeeds(String ownerFeeds) { this.ownerFeeds = ownerFeeds; }
    public String getCaption() { return caption; }
    public int getLikes() { return likes; }
    public int getComments() { return comments; }
    public int getShares() { return shares; }
}