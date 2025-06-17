package com.example.tuprak;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tuprak.models.User;

import java.util.Random;

public class InitializationManager {
    private static final String TAG = "InitializationManager";
    private static final String PREF_NAME = "init_preferences";
    private static final String KEY_INITIALIZED = "app_initialized";
    
    private final Context context;
    private final SharedPreferences preferences;
    
    public InitializationManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public boolean isInitialized() {
        return preferences.getBoolean(KEY_INITIALIZED, false);
    }
    
    public void initialize() {
        if (!isInitialized()) {
            createDummyUsers();
            
            createDummyPosts();
            
            preferences.edit().putBoolean(KEY_INITIALIZED, true).apply();
            
        }
    }
    
    private void createDummyUsers() {
        UserManager userManager = new UserManager(context);
        
        createDummyUser(userManager, "travel_explorer", "Travel Explorer", "R.drawable.profile_travel", 
                "Travel Enthusiast", "Exploring the world one country at a time", 
                "Sharing my adventures with you", "travelblog.com", true);
        
        createDummyUser(userManager, "foodie_delights", "Foodie Delights", "R.drawable.profile_food", 
                "Food Blogger", "Recipe developer & food photographer", 
                "Bringing delicious recipes to your home", "foodiedelights.com", true);
        
        createDummyUser(userManager, "selfie_queen", "Selfie Queen", "R.drawable.profile_selfie", 
                "Lifestyle Influencer", "Beauty | Fashion | Lifestyle", 
                "Living my best life ✨", "lifewithselfie.com", true);
        
        createDummyUser(userManager, "squad_goals", "Squad Goals", "R.drawable.profile_group", 
                "Friends & Adventures", "Making memories with my best friends", 
                "Life is better with friends", "squadgoals.com", false);
        
        createDummyUser(userManager, "paws_and_claws", "Paws & Claws", "R.drawable.profile_pet", 
                "Pet Enthusiast", "Dog mom & cat lover", 
                "Sharing my furry family", "pawsandclaws.com", false);
        
        createDummyUser(userManager, "fit_lifestyle", "Fit Lifestyle", "R.drawable.profile_fitness", 
                "Fitness Coach", "Helping you achieve your fitness goals", 
                "Healthy mind, healthy body", "fitlifestyle.com", true);
        
        createDummyUser(userManager, "creative_soul", "Creative Soul", "R.drawable.profile_art", 
                "Artist & Creator", "Painting | Drawing | Mixed Media", 
                "Creating art that speaks to the soul", "creativesoul.art", false);
        
        createDummyUser(userManager, "nature_lover", "Nature Lover", "R.drawable.profile_nature", 
                "Outdoor Enthusiast", "Hiking | Camping | Wildlife", 
                "Finding peace in nature", "naturelover.com", false);
        
        createDummyUser(userManager, "urban_shots", "Urban Shots", "R.drawable.profile_urban", 
                "Urban Photographer", "Capturing city life through my lens", 
                "The beauty of concrete jungles", "urbanshots.photo", true);
        
        createDummyUser(userManager, "daily_moments", "Daily Moments", "R.drawable.profile_everyday", 
                "Lifestyle & Daily Inspiration", "Finding joy in everyday moments", 
                "Sharing my daily journey", "dailymoments.life", false);
    }
    
    private void createDummyUser(UserManager userManager, String username, String fullName, 
                              String profileImage, String bio1, String bio2, 
                              String bio3, String website, boolean verified) {
        if (userManager.getUserByUsername(username) == null) {
            User user = new User(username, fullName);
            user.setProfileImageUri(profileImage);
            user.setBioLine1(bio1);
            user.setBioLine2(bio2);
            user.setBioLine3(bio3);
            user.setWebsite(website);
            
            Random random = new Random(username.hashCode());
            user.setFollowersCount(1000 + random.nextInt(9000));
            user.setFollowingCount(200 + random.nextInt(800));
            user.setVerified(verified);
            
            userManager.addUser(user);
            Log.d(TAG, "Created dummy user: " + username);
        }
    }
    
    private void createDummyPosts() {
        PostManager postManager = new PostManager(context);
        UserManager userManager = new UserManager(context);
        
        String[] usernames = {
            "travel_explorer", "foodie_delights", "selfie_queen", "squad_goals", 
            "paws_and_claws", "fit_lifestyle", "creative_soul", "nature_lover", 
            "urban_shots", "daily_moments"
        };
        
        for (String username : usernames) {
            User user = userManager.getUserByUsername(username);
            if (user != null) {
                String profileImageUri = user.getProfileImageUri();
                postManager.createDummyPostsForUser(username, profileImageUri, 3);
            }
        }
    }
}