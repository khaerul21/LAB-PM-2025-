package com.example.ig_profile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {
    private String profileImageUriString;
    private String username;
    private String name;
    private String bio;
    private final int followers;
    private final int following;
    private int posts;
    private final List<FeedItem> feedItems;
    private final List<HighlightItem> highlightItems;
    private List<StoryItem> storyItems;

    public User(String profileImageUri, String username, String name, String bio, int followers, int following, List<FeedItem> feedItems, List<HighlightItem> highlightItems) {
        this.profileImageUriString = profileImageUri;
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.followers = followers;
        this.following = following;
        this.feedItems = feedItems;
        this.posts = feedItems.size();
        this.highlightItems = highlightItems;
    }

    protected User(Parcel in) {
        profileImageUriString = in.readString();
        username = in.readString();
        name = in.readString();
        bio = in.readString();
        followers = in.readInt();
        following = in.readInt();
        posts = in.readInt();
        feedItems = in.createTypedArrayList(FeedItem.CREATOR);
        highlightItems = in.createTypedArrayList(HighlightItem.CREATOR);
        storyItems = in.createTypedArrayList(StoryItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileImageUriString);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(bio);
        dest.writeInt(followers);
        dest.writeInt(following);
        dest.writeInt(posts);
        dest.writeTypedList(feedItems);
        dest.writeTypedList(highlightItems);
        dest.writeTypedList(storyItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getProfileImageUriString() { return profileImageUriString; }
    public void setProfileImageUriString(String profileImageUriString) { this.profileImageUriString = profileImageUriString; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getBio() { return bio; }
    public int getFollowers() { return followers; }
    public int getFollowing() { return following; }
    public int getPosts() { return posts; }
    public List<FeedItem> getFeedItems() { return feedItems; }
    public List<HighlightItem> getHighlightItems() { return highlightItems; }
    public List<StoryItem> getStoryItems() { return storyItems; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setBio(String bio) { this.bio = bio; }
    public void addFeedItem(FeedItem feedItem) {
        this.feedItems.add(0, feedItem);
        this.posts = feedItems.size();
    }
    public void setStoryItems(List<StoryItem> storyItems) {
        this.storyItems = storyItems;
    }
}