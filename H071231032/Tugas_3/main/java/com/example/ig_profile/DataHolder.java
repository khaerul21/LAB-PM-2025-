package com.example.ig_profile;

import com.example.ig_profile.models.User;

public class DataHolder {
    private static final DataHolder instance = new DataHolder();
    private User currentUser;

    public static DataHolder getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
