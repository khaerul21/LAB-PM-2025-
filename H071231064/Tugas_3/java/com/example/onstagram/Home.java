package com.example.onstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Home extends AppCompatActivity {

    private RecyclerView storyRecyclerView, feedRecyclerView;
    private StoryAdapter storyAdapter;
    private FeedAdapter feedAdapter;
    private List<Story> storyList;
    private List<Feed> feedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        List<User> users = generateDummyUsers();
        feedList = generateDummyFeeds(users);
        storyList = generateDummyStories(users);

        storyRecyclerView = findViewById(R.id.storyRecyclerView);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        storyAdapter = new StoryAdapter(this, storyList, users, feedList);
        storyRecyclerView.setAdapter(storyAdapter);

        feedRecyclerView = findViewById(R.id.feedRecyclerView);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedAdapter = new FeedAdapter(this, feedList, users);
        feedRecyclerView.setAdapter(feedAdapter);
    }

    private List<User> generateDummyUsers() {
        List<User> users = new ArrayList<>();

        String[] loremIpsumWords = {
                "Lorem", "Yousranmz", "dolor", "sit", "amet",
                "consectetur", "adipiscing", "elit", "sed", "do"
        };

        for (int i = 0; i < 10; i++) {
            String username = loremIpsumWords[i];

            String profileImage = "file:///android_asset/profil_" + i + ".png";

            User user = new User(username, profileImage, 1000, 500, 0);
            users.add(user);
        }

        return users;
    }



    private List<Feed> generateDummyFeeds(List<User> users) {
        List<Feed> feeds = new ArrayList<>();

        for (User user : users) {
            int feedCount = 1 + (int)(Math.random() * 3);
            for (int j = 0; j < feedCount; j++) {
                feeds.add(new Feed(
                        "https://picsum.photos/seed/" + user.getUsername() + "_feed" + j + "/500/300",
                        "Caption " + j + " from " + user.getUsername(),
                        user.getUsername()
                ));
            }
            user.setPostCount(feedCount);
        }

        return feeds;
    }

    private List<Story> generateDummyStories(List<User> users) {
        List<Story> stories = new ArrayList<>();

        for (User user : users) {
            int storyCount = 1;
            for (int j = 0; j < storyCount; j++) {
                stories.add(new Story(
                        "https://picsum.photos/seed/" + user.getUsername() + "_story" + j + "/200",
                        user.getUsername()
                ));
            }
        }

        return stories;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Feed newFeed = data.getParcelableExtra("newFeed");
            User updatedUser = data.getParcelableExtra("user");

            if (newFeed != null && updatedUser != null) {
                feedList.add(0, newFeed);
                feedAdapter.notifyItemInserted(0);
                feedRecyclerView.scrollToPosition(0);
            }
        }
    }

}
