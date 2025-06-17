package com.example.tuprak;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.tuprak.models.Post;
import com.example.tuprak.models.User;
import com.example.tuprak.models.UserPost;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PostManager {

    private static final String TAG = "PostManager";
    private static final String PREF_NAME = "post_preferences";
    private static final String KEY_POSTS = "user_posts";
    private static final String POST_IMAGES_DIR = "post_images";

    private final SharedPreferences sharedPreferences;
    private final Context context;
    private final Gson gson;

    public PostManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    private File getPostImagesDir() {
        File dir = new File(context.getFilesDir(), POST_IMAGES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private Uri savePostImage(Uri sourceUri, String postId) {
        try {
            File imagesDir = getPostImagesDir();
            File destFile = new File(imagesDir, postId + ".jpg");
            
            InputStream in = context.getContentResolver().openInputStream(sourceUri);
            OutputStream out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            
            return Uri.fromFile(destFile);
        } catch (IOException e) {
            Log.e(TAG, "Failed to save post image", e);
            return null;
        }
    }

    public String saveNewPost(Uri imageUri, String username, Uri profileImageUri, String caption) {
        String postId = UUID.randomUUID().toString();
        
        Uri persistentUri = savePostImage(imageUri, postId);
        if (persistentUri == null) {
            Log.e(TAG, "Failed to save post image");
            return null;
        }

        List<UserPost> posts = getUserPosts();
        
        UserManager userManager = new UserManager(context);
        User user = userManager.getUserByUsername(username);
        String userId = (user != null) ? user.getId() : username;


        UserPost newPost = new UserPost(
                postId,
                username,
                userId,
                profileImageUri != null ? profileImageUri.toString() : "",
                persistentUri.toString(),
                caption,
                0,
                0,
                new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date())
        );

        posts.add(0, newPost); 

        savePosts(posts);
        
        if (user != null) {
            user.setPostsCount(user.getPostsCount() + 1);
            userManager.updateUser(user);
        }
        
        return postId;
    }

    public List<UserPost> getPostsByUserId(String userId) {
        List<UserPost> allPosts = getUserPosts();
        List<UserPost> userPosts = new ArrayList<>();

        for (UserPost post : allPosts) {
            if ((post.getUserId() != null && post.getUserId().equals(userId)) || 
                (post.getUserId() == null && post.getUsername().equals(userId))) {
                userPosts.add(post);
            }
        }

        return userPosts;
    }

    public List<UserPost> getPostsByUsername(String username) {
        List<UserPost> allPosts = getUserPosts();
        List<UserPost> userPosts = new ArrayList<>();

        for (UserPost post : allPosts) {
            if (post.getUsername().equals(username)) {
                userPosts.add(post);
            }
        }        
        return userPosts;
    }

    public List<UserPost> getUserPosts() {
        String postsJson = sharedPreferences.getString(KEY_POSTS, "");
        if (postsJson.isEmpty()) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<UserPost>>() {}.getType();
        List<UserPost> posts = gson.fromJson(postsJson, type);

        return posts != null ? posts : new ArrayList<>();
    }

    public void createDummyPostsForUser(String username, String profileImageUri, int count) {
        UserManager userManager = new UserManager(context);
        User user = userManager.getUserByUsername(username);
        String userId = (user != null) ? user.getId() : username;
        
        Random random = new Random(username.hashCode());
        
        removeExistingPostsForUser(userId);
        
        int postsToCreate = 1 + Math.abs(username.hashCode() % 3);
        postsToCreate = Math.min(postsToCreate, count);
        
        String[][] allCaptions = {
            // Travel captions
            {
                "Exploring new horizons ✈️ #travel #adventure",
                "Lost in paradise 🏝️ The view is worth every step!",
                "Take me back to this magical place 😍"
            },
            // Food captions
            {
                "Brunch goals 🍳 #foodie",
                "Homemade goodness! Recipe in bio 👩‍🍳",
                "Best coffee in town ☕ Worth the wait!"
            },
            // Selfie captions
            {
                "Good vibes only ✨",
                "New hair, who dis? 💇‍♀️",
                "Friday mood 😎 #weekend"
            },
            // Group captions
            {
                "Squad goals 👯‍♀️ #bestfriends",
                "Making memories with these people ❤️",
                "Throwback to better times 🎉 #tbt"
            },
            // Pet captions
            {
                "Meet my new fur baby 🐶 #dogsofinstagram",
                "Someone wants treats 🐱 #catsofinstagram",
                "Those eyes though... 🥺"
            },
            // Fitness captions
            {
                "Rise and grind 💪 #morningworkout",
                "Personal best today! 🏋️‍♀️ #fitness",
                "Mind over matter 🧘‍♂️ #wellness"
            },
            // Art captions
            {
                "Latest project finally complete 🎨 #art",
                "Getting creative today 🖌️ #artist",
                "Work in progress... thoughts? 👀"
            },
            // Nature captions
            {
                "Mother Nature showing off 🌿 #nature",
                "Breathtaking sunset tonight 🌅",
                "Found this little beauty on my walk 🌸 #flowers"
            },
            // Urban captions
            {
                "Urban jungle 🏙️ #citylife",
                "Architecture appreciation post 🏛️",
                "Concrete jungle where dreams are made of 🌃"
            },
            // Everyday captions
            {
                "Coffee and productivity 💻 #workday",
                "New purchase! So happy with it ✨ #shopping",
                "Daily essentials 👜 #flatlay"
            }
        };

        int captionSetIndex = 0;
        int[] postImages;
        String profileImage;
        
        switch (username) {
            case "travel_explorer":
                captionSetIndex = 0; // Travel
                postImages = new int[] {
                    R.drawable.post_travel_1,
                    R.drawable.post_travel_2,
                    R.drawable.post_travel_3
                };
                profileImage = "R.drawable.profile_travel";
                break;
                
            case "foodie_delights":
                captionSetIndex = 1; // Food
                postImages = new int[] {
                    R.drawable.post_food_1,
                    R.drawable.post_food_2,
                    R.drawable.post_food_3
                };
                profileImage = "R.drawable.profile_food";
                break;
                
            case "selfie_queen":
                captionSetIndex = 2; // Selfie
                postImages = new int[] {
                    R.drawable.post_selfie_1,
                    R.drawable.post_selfie_2,
                    R.drawable.post_selfie_3
                };
                profileImage = "R.drawable.profile_selfie";
                break;
                
            case "squad_goals":
                captionSetIndex = 3; // Group
                postImages = new int[] {
                    R.drawable.post_group_1,
                    R.drawable.post_group_2,
                    R.drawable.post_group_3
                };
                profileImage = "R.drawable.profile_group";
                break;
                
            case "paws_and_claws":
                captionSetIndex = 4; // Pet
                postImages = new int[] {
                    R.drawable.post_pet_1,
                    R.drawable.post_pet_2,
                    R.drawable.post_pet_3
                };
                profileImage = "R.drawable.profile_pet";
                break;
                
            case "fit_lifestyle":
                captionSetIndex = 5; // Fitness
                postImages = new int[] {
                    R.drawable.post_fitness_1,
                    R.drawable.post_fitness_2,
                    R.drawable.post_fitness_3
                };
                profileImage = "R.drawable.profile_fitness";
                break;
                
            case "creative_soul":
                captionSetIndex = 6; // Art
                postImages = new int[] {
                    R.drawable.post_art_1,
                    R.drawable.post_art_2,
                    R.drawable.post_art_3
                };
                profileImage = "R.drawable.profile_art";
                break;
                
            case "nature_lover":
                captionSetIndex = 7; // Nature
                postImages = new int[] {
                    R.drawable.post_nature_1,
                    R.drawable.post_nature_2,
                    R.drawable.post_nature_3
                };
                profileImage = "R.drawable.profile_nature";
                break;
                
            case "urban_shots":
                captionSetIndex = 8; // Urban
                postImages = new int[] {
                    R.drawable.post_urban_1,
                    R.drawable.post_urban_2,
                    R.drawable.post_urban_3
                };
                profileImage = "R.drawable.profile_urban";
                break;
                
            case "daily_moments":
                captionSetIndex = 9; // Everyday
                postImages = new int[] {
                    R.drawable.post_everyday_1,
                    R.drawable.post_everyday_2,
                    R.drawable.post_everyday_3
                };
                profileImage = "R.drawable.profile_everyday";
                break;
                
            default:
                if (Math.abs(username.hashCode() % 10) <= 3) {
                    postImages = new int[] {
                        R.drawable.post_tech_1,
                        R.drawable.post_tech_2,
                        R.drawable.post_tech_3
                    };
                } else if (Math.abs(username.hashCode() % 10) <= 6) {
                    postImages = new int[] {
                        R.drawable.post_design_1,
                        R.drawable.post_design_2,
                        R.drawable.post_design_3
                    };
                } else {
                    postImages = new int[] {
                        R.drawable.post_photo_1,
                        R.drawable.post_photo_2,
                        R.drawable.post_photo_3
                    };
                }
                profileImage = profileImageUri;
                break;
        }
        
        // Create posts
        for (int i = 0; i < postsToCreate; i++) {
            String caption = allCaptions[captionSetIndex][i % 3];
            
            int likesCount = 50 + random.nextInt(950); 
            int commentsCount = 5 + random.nextInt(95);
            
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -(i * 3 + 1));
            String timestamp = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                .format(calendar.getTime());
            
            String finalProfileImageUri = profileImageUri;
            if (profileImage != profileImageUri) {
                finalProfileImageUri = "android.resource://" + context.getPackageName() + "/" + 
                    profileImage.replace("R.drawable.", "");
            }
            
            UserPost newPost = new UserPost(
                UUID.randomUUID().toString(),             
                username,                                
                userId,                                 
                finalProfileImageUri,                    
                "android.resource://" + context.getPackageName() + "/" + postImages[i % postImages.length], // postImageUri
                caption,                                
                likesCount,                          
                commentsCount,                          
                timestamp                          
            );
            
            addPost(newPost);
        }
        
        if (user != null) {
            user.setPostsCount(postsToCreate);
            userManager.updateUser(user);
        }
    }

    private void removeExistingPostsForUser(String userId) {
        List<UserPost> allPosts = getUserPosts();
        List<UserPost> postsToKeep = new ArrayList<>();
        
        for (UserPost post : allPosts) {
            if (post.getUserId() == null || !post.getUserId().equals(userId)) {
                postsToKeep.add(post);
            }
        }
        
        savePosts(postsToKeep);
    }

    public void savePosts(List<UserPost> posts) {
        String postsJson = gson.toJson(posts);
        sharedPreferences.edit().putString(KEY_POSTS, postsJson).apply();
    }

    public void addPost(UserPost post) {
        List<UserPost> allPosts = getUserPosts();
        allPosts.add(post);
        savePosts(allPosts);
    }

    public void migrateExistingPosts() {
        List<UserPost> allPosts = getUserPosts();
        boolean needsMigration = false;
        
        for (UserPost post : allPosts) {
            if (post.getUserId() == null) {
                needsMigration = true;
                break;
            }
        }
        
        if (needsMigration) {
            List<UserPost> migratedPosts = new ArrayList<>();
            UserManager userManager = new UserManager(context);
            
            for (UserPost post : allPosts) {

                User user = userManager.getUserByUsername(post.getUsername());
                String userId = (user != null) ? user.getId() : post.getUsername();
                
                UserPost migratedPost = new UserPost(
                    post.getId(),
                    post.getUsername(),
                    userId,
                    post.getUserProfileImageUri(),
                    post.getPostImageUri(),
                    post.getCaption(),
                    post.getLikesCount(),
                    post.getCommentsCount(),
                    post.getTimePosted()
                );
                
                migratedPosts.add(migratedPost);
            }
            
            savePosts(migratedPosts);
        }
    }
}