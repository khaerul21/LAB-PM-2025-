package com.example.revisipraktikum02;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    private String name;
    private String userId;
    private String password;
    private String email;
    private String imageUri;

    public UserProfile(String name, String userId, String password, String email, String imageUri) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.imageUri = imageUri;
    }

    //Parcel Constractor
    protected UserProfile(Parcel in) {
        name = in.readString();
        userId = in.readString();
        password = in.readString();
        email = in.readString();
        imageUri = in.readString();
    }

    //Creator
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


    // Getter
    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUri() {
        return imageUri;
    }

    // Setter (opsional, kalau kamu mau ubah datanya nanti)
    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    //Write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(userId);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(imageUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
