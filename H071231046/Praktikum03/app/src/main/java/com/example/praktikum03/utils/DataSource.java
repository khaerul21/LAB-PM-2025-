package com.example.praktikum03.utils;

import android.util.Log;

import com.example.praktikum03.R;
import com.example.praktikum03.models.Post;
import com.example.praktikum03.models.Story;
import com.example.praktikum03.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataSource {
    private static List<User> userList = new ArrayList<>();
    private static List<Post> feedPostList = new ArrayList<>();
    private static List<Post> profilePostList = new ArrayList<>();
    private static List<Story> storyList = new ArrayList<>();
    private static User currentUser;

    static {
        try {
            initUsers();
            initFeedPosts();
            initProfilePosts();
            initStories();
        } catch (Exception e) {
            Log.e("DataSource", "Error initializing data: " + e.getMessage());
        }
    }

    private static void initUsers() {
        currentUser = new User(
                1,
                "Celzi",
                R.drawable.ic_profilenew,
                "Chelsea",
                "Blessed with the best",
                1111,
                1,
                6
        );
        userList.add(new User(2, "hanako", R.drawable.ic_profilehanako, "Hanako", "Love chelsea", 999345, 15, 1));
        userList.add(new User(3,"yuno", R.drawable.ic_profileyuno, "Yuno", "Mengapa dia meninggalkanku?",99999,17,1));
        userList.add(new User(4,"mirai",R.drawable.ic_profilemirai,"Mirai","Butuh dia...",77777,8,1));
        userList.add(new User(5,"shizuku",R.drawable.ic_shizuku,"Shizuku","No bio",757557,8,1));
        userList.add(new User(6,"asta",R.drawable.ic_asta,"Asta","Belajar untuk menjadi kastria sihir",88888,8,1));
        userList.add(new User(7,"gojo",R.drawable.ic_gojo,"Gojo","Need hug",77777,8,1));
        userList.add(new User(8,"dazai_osamu",R.drawable.ic_dazaiosamu,"Dazai Osamu","Apa liat2?",890,8,1));
        userList.add(new User(9,"charmie",R.drawable.ic_charmie,"Charmie","Jangan lupa makan ya...",88888,8,1));
        userList.add(currentUser);
    }

    private static void initFeedPosts() { //bagian mainactivity yg post-an orgorg
        feedPostList.add(new Post(1, userList.get(4), R.drawable.post1, "no caption", 20,2,6,new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(17))));
        feedPostList.add(new Post(2, userList.get(7), R.drawable.post2, "star", 85,19,9, new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))));
        feedPostList.add(new Post(3, userList.get(0), R.drawable.post3, "jadi cewe dlu nggk sih", 120, 20,9, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2))));
        feedPostList.add(new Post(4, userList.get(2), R.drawable.post4, "aib katanya", 99, 10, 3, new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(4))));
        feedPostList.add(new Post(5, userList.get(3), R.drawable.post5, "need?", 815, 22,3,new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))));
        feedPostList.add(new Post(6, userList.get(4), R.drawable.post6, "healing dlu nggk sih", 23,2,4, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(23))));
        feedPostList.add(new Post(7, userList.get(5), R.drawable.post7, "butuh apa beb?", 999999, 999999,999999,new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(20))));
        feedPostList.add(new Post(8, userList.get(6), R.drawable.post8, "big love to you", 400,80,90, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))));
//        feedPostList.add(new Post(9, userList.get(8), R.drawable.post9, "rikurikuriku", 800, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))));
    }

    private static void initProfilePosts() { //my feed ini
        profilePostList.add(new Post(111, currentUser, R.drawable.post_feed1, "menurutmu", 10,1,29, new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(20))));
        profilePostList.add(new Post(112, currentUser, R.drawable.post_feed2, "lucu", 20,2,19, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5))));
        profilePostList.add(new Post(113, currentUser, R.drawable.post_feed3, "pengen nyulik dua2nya", 1,560,39, new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(3))));
        profilePostList.add(new Post(114, currentUser, R.drawable.post_feed4, "kue dari hanako guys", 5, 10,29,new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5))));
        profilePostList.add(new Post(115, currentUser, R.drawable.post_feed5, "no capt", 100,2,2, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2))));
        profilePostList.add(new Post(116, currentUser, R.drawable.post_feed6, "love", 25, 1,8,new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5))));
        profilePostList.add(new Post(117, currentUser, R.drawable.post_feed7, "with bebe", 45, 20,9,new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2))));
        profilePostList.add(new Post(118, currentUser, R.drawable.post_feed8, "my draw", 15, 5,7,new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5))));
        profilePostList.add(new Post(119, currentUser, R.drawable.post_feed9,"lastbutnotleast", 125,4,2, new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2))));
        currentUser.setPostsCount(profilePostList.size());
    }

    private static void initStories() {
        storyList.add(new Story(
                1,
                "beach",
                R.drawable.storythumbnile1,
                Arrays.asList(
                        R.drawable.story_post1,
                        R.drawable.story_post2,
                        R.drawable.story_post3,
                        R.drawable.story_post4,
                        R.drawable.story_post5
                ),
                new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
        ));
        storyList.add(new Story(
                2,
                "sunset",
                R.drawable.storythumbnile2,
                Arrays.asList(
                        R.drawable.story_post6,
                        R.drawable.story_post7,
                        R.drawable.story_post8,
                        R.drawable.story_post9,
                        R.drawable.story_post10
                ),
                new Date(
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(20)
                )
        ));
    }



    public static List<User> getUserList() {
        return userList;
    }
    public static List<Post> getFeedPostList() {
        return feedPostList;
    }
    public static List<Post> getProfilePostList() {
        return profilePostList;
    }
    public static List<Story> getStoryList() {
        return storyList;
    }
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void addPost(Post post) {
        profilePostList.add(0, post);
        currentUser.setPostsCount(profilePostList.size());
    }

    public static List<Post> getPostsByUser(User user) {
        List<Post> result = new ArrayList<>();
        for (Post post : feedPostList) {
            if (post.getUser().getId() == user.getId()) {
                result.add(post);
            }
        }

        if (user.getId() == currentUser.getId()) {
            result.addAll(profilePostList);
        }

        return result;
    }
}