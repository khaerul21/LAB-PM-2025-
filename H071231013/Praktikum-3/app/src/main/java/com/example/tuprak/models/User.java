package com.example.tuprak.models;

import android.net.Uri;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String profileImageUri;
    private String bioLine1;
    private String bioLine2;
    private String bioLine3;
    private String website;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private boolean isVerified;

    public User(String username, String fullName) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.fullName = fullName;
        this.followersCount = 0;
        this.followingCount = 0;
        this.postsCount = 0;
        this.isVerified = false;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getProfileImageUri() { return profileImageUri; }
    public void setProfileImageUri(String profileImageUri) { this.profileImageUri = profileImageUri; }
    
    public String getBioLine1() { return bioLine1; }
    public void setBioLine1(String bioLine1) { this.bioLine1 = bioLine1; }
    
    public String getBioLine2() { return bioLine2; }
    public void setBioLine2(String bioLine2) { this.bioLine2 = bioLine2; }
    
    public String getBioLine3() { return bioLine3; }
    public void setBioLine3(String bioLine3) { this.bioLine3 = bioLine3; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public int getPostsCount() { return postsCount; }
    public void setPostsCount(int postsCount) { this.postsCount = postsCount; }
    
    public int getFollowersCount() { return followersCount; }
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }
    
    public int getFollowingCount() { return followingCount; }
    public void setFollowingCount(int followingCount) { this.followingCount = followingCount; }
    
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    
    public String getBioText() {
        StringBuilder bio = new StringBuilder();
        if (bioLine1 != null) bio.append(bioLine1);
        bio.append("\n");
        if (bioLine2 != null) bio.append(bioLine2);
        bio.append("\n");
        if (bioLine3 != null) bio.append(bioLine3);
        return bio.toString();
    }
}