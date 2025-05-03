package com.example.tp_1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    private String username;
    private String name;
    private String pronouns;
    private String bio;
    private String profilePhotoUri;

    // Constructor
    public UserProfile(String username, String name, String pronouns, String bio, String profilePhotoUri) {
        this.username = username;
        this.name = name;
        this.pronouns = pronouns;
        this.bio = bio;
        this.profilePhotoUri = profilePhotoUri;
    }

    // Parcelable Constructor
    protected UserProfile(Parcel in) {
        username = in.readString();
        name = in.readString();
        pronouns = in.readString();
        bio = in.readString();
        profilePhotoUri = in.readString();
    }

    // CREATOR for Parcelable
    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    // Getter methods
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getPronouns() { return pronouns; }
    public String getBio() { return bio; }
    public String getProfilePhotoUri() { return profilePhotoUri; }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        this.profilePhotoUri = profilePhotoUri;
    }

    // Save UserProfile to SharedPreferences
    public void saveToSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfilePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", this.username);
        editor.putString("name", this.name);
        editor.putString("pronouns", this.pronouns);
        editor.putString("bio", this.bio);
        editor.putString("profilePhotoUri", this.profilePhotoUri);
        editor.apply();
    }

    // Load UserProfile from SharedPreferences
    public static UserProfile loadFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserProfilePrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String name = sharedPreferences.getString("name", "");
        String pronouns = sharedPreferences.getString("pronouns", "");
        String bio = sharedPreferences.getString("bio", "");
        String profilePhotoUri = sharedPreferences.getString("profilePhotoUri", "");

        return new UserProfile(username, name, pronouns, bio, profilePhotoUri);
    }

    // Parcelable Methods
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(pronouns);
        parcel.writeString(bio);
        parcel.writeString(profilePhotoUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

