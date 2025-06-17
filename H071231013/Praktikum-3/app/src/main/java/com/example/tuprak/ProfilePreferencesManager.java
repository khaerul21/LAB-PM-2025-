package com.example.tuprak;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class ProfilePreferencesManager {
    private static final String PREF_NAME = "profile_preferences";
    
    private static final String KEY_PROFILE_NAME = "profile_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_BIO_LINE1 = "bio_line1";
    private static final String KEY_BIO_LINE2 = "bio_line2";
    private static final String KEY_BIO_LINE3 = "bio_line3";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_KATA_GANTI = "kata_ganti";
    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";
    
    private final SharedPreferences sharedPreferences;
    
    public ProfilePreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveProfileName(String name) {
        sharedPreferences.edit().putString(KEY_PROFILE_NAME, name).apply();
    }
    
    public String getProfileName(String defaultValue) {
        return sharedPreferences.getString(KEY_PROFILE_NAME, defaultValue);
    }
    
    public void saveUsername(String username) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
    }
    
    public String getUsername(String defaultValue) {
        return sharedPreferences.getString(KEY_USERNAME, defaultValue);
    }
    
    public void saveBioLine1(String bioLine) {
        sharedPreferences.edit().putString(KEY_BIO_LINE1, bioLine).apply();
    }
    
    public void saveBioLine2(String bioLine) {
        sharedPreferences.edit().putString(KEY_BIO_LINE2, bioLine).apply();
    }
    
    public void saveBioLine3(String bioLine) {
        sharedPreferences.edit().putString(KEY_BIO_LINE3, bioLine).apply();
    }
    
    public String getBioLine1(String defaultValue) {
        return sharedPreferences.getString(KEY_BIO_LINE1, defaultValue);
    }
    
    public String getBioLine2(String defaultValue) {
        return sharedPreferences.getString(KEY_BIO_LINE2, defaultValue);
    }
    
    public String getBioLine3(String defaultValue) {
        return sharedPreferences.getString(KEY_BIO_LINE3, defaultValue);
    }
    
    public void saveWebsite(String website) {
        sharedPreferences.edit().putString(KEY_WEBSITE, website).apply();
    }
    
    public String getWebsite(String defaultValue) {
        return sharedPreferences.getString(KEY_WEBSITE, defaultValue);
    }
    
    public void saveGender(String gender) {
        sharedPreferences.edit().putString(KEY_GENDER, gender).apply();
    }
    
    public String getGender(String defaultValue) {
        return sharedPreferences.getString(KEY_GENDER, defaultValue);
    }
    
    public void saveKataGanti(String kataGanti) {
        sharedPreferences.edit().putString(KEY_KATA_GANTI, kataGanti).apply();
    }
    
    public String getKataGanti(String defaultValue) {
        return sharedPreferences.getString(KEY_KATA_GANTI, defaultValue);
    }
    
    public void saveProfileImageUri(Uri uri) {
        if (uri != null) {
            sharedPreferences.edit().putString(KEY_PROFILE_IMAGE_URI, uri.toString()).apply();
        } else {
            sharedPreferences.edit().remove(KEY_PROFILE_IMAGE_URI).apply();
        }
    }
    
    public Uri getProfileImageUri() {
        String uriString = sharedPreferences.getString(KEY_PROFILE_IMAGE_URI, null);
        if (uriString != null && !uriString.isEmpty()) {
            return Uri.parse(uriString);
        }
        return null;
    }
    
    public void saveBio(String bio) {
        if (bio != null) {
            String[] lines = bio.split("\n", -1);
            
            saveBioLine1(lines.length > 0 ? lines[0] : "");
            saveBioLine2(lines.length > 1 ? lines[1] : "");
            saveBioLine3(lines.length > 2 ? lines[2] : "");
        } else {
            saveBioLine1("");
            saveBioLine2("");
            saveBioLine3("");
        }
    }
    
    public String getBio() {
        StringBuilder bio = new StringBuilder();
        bio.append(getBioLine1(""));
        bio.append("\n");
        bio.append(getBioLine2(""));
        bio.append("\n");
        bio.append(getBioLine3(""));
        
        return bio.toString();
    }
}