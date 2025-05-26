package com.example.tuprak;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.tuprak.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserManager {
    private static final String TAG = "UserManager";
    private static final String PREF_NAME = "user_data";
    private static final String KEY_USERS = "users";
    private static final String KEY_CURRENT_USER = "current_user";
    
    private final SharedPreferences sharedPreferences;
    private final Context context;
    private final Gson gson;
    private Map<String, User> userMap; // username -> User
    
    public UserManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
        loadUsers();
        
        if (userMap.isEmpty()) {
            createDummyUsers();
        }
    }
    
    private void loadUsers() {
        String usersJson = sharedPreferences.getString(KEY_USERS, null);
        if (usersJson != null) {
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            userMap = gson.fromJson(usersJson, type);
        } else {
            userMap = new HashMap<>();
        }
    }
    
    private void saveUsers() {
        String usersJson = gson.toJson(userMap);
        sharedPreferences.edit().putString(KEY_USERS, usersJson).apply();
    }
    
    public User getCurrentUser() {
        String username = sharedPreferences.getString(KEY_CURRENT_USER, null);
        if (username != null && userMap.containsKey(username)) {
            return userMap.get(username);
        }
        
        ProfilePreferencesManager profileManager = new ProfilePreferencesManager(context);
        String currentUsername = profileManager.getUsername("user");
        String currentName = profileManager.getProfileName("User");
        
        User currentUser = new User(currentUsername, currentName);
        currentUser.setBioLine1(profileManager.getBioLine1(""));
        currentUser.setBioLine2(profileManager.getBioLine2(""));
        currentUser.setBioLine3(profileManager.getBioLine3(""));
        currentUser.setWebsite(profileManager.getWebsite(""));
        
        Uri profileImageUri = profileManager.getProfileImageUri();
        if (profileImageUri != null) {
            currentUser.setProfileImageUri(profileImageUri.toString());
        }
        
        userMap.put(currentUsername, currentUser);
        saveUsers();
        setCurrentUser(currentUsername);
        
        return currentUser;
    }
    
    public void setCurrentUser(String username) {
        sharedPreferences.edit().putString(KEY_CURRENT_USER, username).apply();
    }
    
    public User getUserByUsername(String username) {
        return userMap.getOrDefault(username, null);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }
    
    public void addUser(User user) {
        userMap.put(user.getUsername(), user);
        saveUsers();
    }
    
    public void updateUser(User user) {
        if (userMap.containsKey(user.getUsername())) {
            userMap.put(user.getUsername(), user);
            saveUsers();
        }
    }
    
    public void deleteUser(String username) {
        if (userMap.containsKey(username)) {
            userMap.remove(username);
            saveUsers();
        }
    }
    
    public boolean isFollowing(String currentUsername, String targetUsername) {
        return false;
    }
    
    public void toggleFollow(String currentUsername, String targetUsername) {
        User targetUser = getUserByUsername(targetUsername);
        if (targetUser != null) {
            boolean following = isFollowing(currentUsername, targetUsername);
            
            if (following) {
                int followers = targetUser.getFollowersCount();
                if (followers > 0) {
                    targetUser.setFollowersCount(followers - 1);
                    updateUser(targetUser);
                }
            } else {
                targetUser.setFollowersCount(targetUser.getFollowersCount() + 1);
                updateUser(targetUser);
            }
        }
    }
    
    private void createDummyUsers() {
        String[] usernames = {
            "travel_explorer", "foodie_delights", "selfie_queen", "squad_goals", 
            "paws_and_claws", "fit_lifestyle", "creative_soul", "nature_lover", 
            "urban_shots", "daily_moments"
        };
        
        String[] fullNames = {
            "Travel Explorer", "Foodie Delights", "Selfie Queen", 
            "Squad Goals", "Paws & Claws", "Fit Lifestyle", 
            "Creative Soul", "Nature Lover", "Urban Shots", "Daily Moments"
        };
        
        String[] bios1 = {
            "Travel Enthusiast", "Food Blogger", "Lifestyle Influencer",
            "Friends & Adventures", "Pet Enthusiast", "Fitness Coach", 
            "Artist & Creator", "Outdoor Enthusiast", "Urban Photographer", "Lifestyle & Daily Inspiration"
        };
        
        String[] bios2 = {
            "Exploring the world one country at a time", "Recipe developer & food photographer", 
            "Beauty | Fashion | Lifestyle", "Making memories with my best friends", 
            "Dog mom & cat lover", "Helping you achieve your fitness goals", 
            "Painting | Drawing | Mixed Media", "Hiking | Camping | Wildlife", 
            "Capturing city life through my lens", "Finding joy in everyday moments"
        };
        
        String[] bios3 = {
            "Sharing my adventures with you", "Bringing delicious recipes to your home", 
            "Living my best life ✨", "Life is better with friends", 
            "Sharing my furry family", "Healthy mind, healthy body", 
            "Creating art that speaks to the soul", "Finding peace in nature", 
            "The beauty of concrete jungles", "Sharing my daily journey"
        };
        
        String[] websites = {
            "travelblog.com", "foodiedelights.com", "lifewithselfie.com", 
            "squadgoals.com", "pawsandclaws.com", "fitlifestyle.com", 
            "creativesoul.art", "naturelover.com", "urbanshots.photo", 
            "dailymoments.life"
        };
        
        String[] imageResources = {
            "profile_travel", "profile_food", "profile_selfie", "profile_group",
            "profile_pet", "profile_fitness", "profile_art", "profile_nature", 
            "profile_urban", "profile_everyday"
        };
        
        boolean[] verified = {
            true, true, true, false, false, true, false, false, true, false
        };
        
        Random random = new Random();
        
        for (int i = 0; i < usernames.length; i++) {
            int followers = 1000 + random.nextInt(9000);
            int following = 200 + random.nextInt(800);
            int posts = 20 + random.nextInt(150);
            
            createDummyUser(
                usernames[i], 
                fullNames[i], 
                imageResources[i], 
                bios1[i], 
                bios2[i], 
                bios3[i],
                websites[i],
                followers, 
                following, 
                posts, 
                verified[i]
            );
        }
    }
    
    private void createDummyUser(String username, String fullName, String imageResourceName, 
                               String bio1, String bio2, String bio3, 
                               String website, int followers, int following, 
                               int posts, boolean verified) {
        User user = new User(username, fullName);
        
        int resourceId = context.getResources().getIdentifier(
            imageResourceName, "drawable", context.getPackageName());
        
        if (resourceId != 0) {
            // Use the numeric resource ID in URI (not the string path)
            String formattedResourceUri = "android.resource://" + context.getPackageName() + 
                    "/" + resourceId;
            user.setProfileImageUri(formattedResourceUri);
        } else {
            int defaultId = R.drawable.lab_logo;
            String formattedResourceUri = "android.resource://" + context.getPackageName() + 
                    "/" + defaultId;
            user.setProfileImageUri(formattedResourceUri);
        }
        
        user.setBioLine1(bio1);
        user.setBioLine2(bio2);
        user.setBioLine3(bio3);
        user.setWebsite(website);
        user.setFollowersCount(followers);
        user.setFollowingCount(following);
        user.setPostsCount(posts);
        user.setVerified(verified);
        
        userMap.put(username, user);
    }
}