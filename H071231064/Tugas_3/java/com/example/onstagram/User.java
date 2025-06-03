package com.example.onstagram;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {
    private String username;
    private String profileImageUrl;
    private Integer followers;
    private Integer following;
    private Integer postCount;

    public User(String username, String profileImageUrl, Integer followers, Integer following, Integer postCount) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.followers = followers;
        this.following = following;
        this.postCount = postCount;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getFollowers() {
        return followers;
    }
    public void setFollowers(Integer followers) {
        this.followers = followers;
    }
    public Integer getFollowing() {
        return following;
    }
    public void setFollowing(Integer following) {
        this.following = following;
    }
    public Integer getPostCount() {
        return postCount;
    }
    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    protected User(Parcel in) {
        username = in.readString();
        profileImageUrl = in.readString();
        postCount = in.readInt();
        followers = in.readInt();
        following = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(profileImageUrl);
        parcel.writeInt(postCount);
        parcel.writeInt(followers);
        parcel.writeInt(following);
    }
}
