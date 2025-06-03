package com.example.onstagram;

import android.os.Parcel;
import android.os.Parcelable;

public class Feed implements Parcelable {
    private String feedImageUrl;
    private String feedCaption;
    private String username;

    public Feed(String feedImageUrl, String feedCaption, String username) {
        this.feedImageUrl = feedImageUrl;
        this.feedCaption = feedCaption;
        this.username = username;
    }
    protected Feed(Parcel in) {
        feedImageUrl = in.readString();
        feedCaption = in.readString();
        username = in.readString();
    }
    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(feedImageUrl);
        parcel.writeString(feedCaption);
        parcel.writeString(username);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public String getFeedImageUrl() {
        return feedImageUrl;
    }
    public String getFeedCaption() {
        return feedCaption;
    }
    public String getUsername(){
        return  username;
    }
    public void setFeedCaption(String feedCaption) {
        this.feedCaption = feedCaption;
    }
}

