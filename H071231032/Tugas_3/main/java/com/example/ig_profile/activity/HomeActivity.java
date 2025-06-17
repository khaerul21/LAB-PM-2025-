package com.example.ig_profile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ig_profile.DataHolder;
import com.example.ig_profile.R;
import com.example.ig_profile.adapter.HomeFeedAdapter;
import com.example.ig_profile.adapter.StoryAdapter;
import com.example.ig_profile.models.FeedItem;
import com.example.ig_profile.models.HighlightItem;
import com.example.ig_profile.models.StoryItem;
import com.example.ig_profile.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private final List<User> userList = new ArrayList<>();
    private User currentUser, guest1, guest2, guest3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerStory = findViewById(R.id.recyclerHighlights);
        RecyclerView recyclerFeed = findViewById(R.id.recyclerFeed);

        recyclerStory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFeed.setLayoutManager(new LinearLayoutManager(this));

        if (DataHolder.getInstance().getCurrentUser() == null) {
            currentUser = getIntent().getParcelableExtra("currentUser", User.class);
        } else {
            currentUser = DataHolder.getInstance().getCurrentUser();
        }

        // Dummy story user yang sedang login
        List<Integer> storyItems = new ArrayList<>();
        storyItems.add(R.drawable.story_3);

        assert currentUser != null;
        StoryItem currentUserStory = new StoryItem(currentUser.getProfileImageUriString(), currentUser.getUsername(), storyItems);
        currentUser.setStoryItems(List.of(currentUserStory));

        // Dummy user lain (guests)
        initDummyUsers();

        userList.add(currentUser);
        userList.add(guest1);
        userList.add(guest2);
        userList.add(guest3);

        // Gabungkan semua feed dan highlight dari seluruh user
        List<FeedItem> allFeeds = new ArrayList<>();
        List<StoryItem> allStories = new ArrayList<>();
        for (User u : userList) {
            for (FeedItem feed : u.getFeedItems()) {
                feed.setOwnerFeeds(u.getUsername()); // pastikan semua feed punya owner
            }

            allFeeds.addAll(u.getFeedItems());
            if (u.getStoryItems() != null) {
                allStories.addAll(u.getStoryItems());
            }
        }

        recyclerStory.setAdapter(new StoryAdapter(allStories, this));
        recyclerFeed.setAdapter(new HomeFeedAdapter(allFeeds, userList, this));

        ImageView ivNewPost = findViewById(R.id.new_post_activity);
        ivNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewPostActivity.class);
            startActivity(intent);
        });

        ImageView ivProfile = findViewById(R.id.profile_activity);
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

    }

    private void initDummyUsers() {
        // Guest 1
        List<FeedItem> guest1Feeds = new ArrayList<>();
        guest1Feeds.add(new FeedItem(R.drawable.feed_guest1, "A", 120, 45, 10));
        guest1Feeds.add(new FeedItem(R.drawable.feed_guest1_2, "B", 1000, 45, 100));

        List<Integer> guest1HighlightItems = new ArrayList<>();
        guest1HighlightItems.add(R.drawable.highlight_1);
        guest1HighlightItems.add(R.drawable.highlight_1_2);
        guest1HighlightItems.add(R.drawable.highlight_1_3);

        List<HighlightItem> guest1Highlight = new ArrayList<>();
        guest1Highlight.add(new HighlightItem(R.drawable.highlight_photos, "A", guest1HighlightItems));

        guest1 = new User(
                (Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile_1)).toString(),
                "guest1",
                "grll",
                ".",
                1234,
                567,
                guest1Feeds,
                guest1Highlight
        );
        List<Integer> guest1StoryItems = new ArrayList<>();
        guest1StoryItems.add(R.drawable.story_1);
        guest1StoryItems.add(R.drawable.story_1_2);

        StoryItem guest1Stories = new StoryItem(guest1.getProfileImageUriString(), guest1.getUsername(), guest1StoryItems);
        guest1.setStoryItems(List.of(guest1Stories));

        // Guest 2
        List<FeedItem> guest2Feeds = new ArrayList<>();
        guest2Feeds.add(new FeedItem(R.drawable.feed_guest2, "Aaa", 1220, 415, 10));
        guest2Feeds.add(new FeedItem(R.drawable.feed_guest2_2, "dasda", 1300, 5, 100));

        List<Integer> guest2HighlightItems = new ArrayList<>();
        guest2HighlightItems.add(R.drawable.highlight_2);

        List<HighlightItem> guest2Highlight = new ArrayList<>();
        guest2Highlight.add(new HighlightItem(R.drawable.highlight_photos, "EMM", guest2HighlightItems));

        guest2 = new User(
                (Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile_2)).toString(),
                "guest2",
                "grll2",
                "adsasdasdas\newr",
                234234,
                23,
                guest2Feeds,
                guest2Highlight
        );
        List<Integer> guest2StoryItems = new ArrayList<>();
        guest2StoryItems.add(R.drawable.story_2);

        StoryItem guest2Stories = new StoryItem(guest2.getProfileImageUriString(), guest2.getUsername(), guest2StoryItems);
        guest2.setStoryItems(List.of(guest2Stories));

        // Guest 3
        List<FeedItem> guest3Feeds = new ArrayList<>();
        guest3Feeds.add(new FeedItem(R.drawable.feed_guest3, "A", 10, 4500, 1));

        guest3 = new User(
                (Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profile_3)).toString(),
                "guest3",
                "sssd",
                "a\ns\nd\nd\nf",
                3411,
                1000000,
                guest3Feeds,
                null
        );

    }
}