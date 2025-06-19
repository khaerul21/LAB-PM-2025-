package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.Story;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FeedAdapter feedAdapter;
    private List<Post> dummyPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dummyPosts = new ArrayList<>();
        setupBottomNavigation();
        setupRecyclerView();
        loadDummyData();
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(this, PostActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        feedAdapter = new FeedAdapter(
                dummyPosts,
                userId -> startActivity(new Intent(this, ProfileActivity.class)),
                post -> {
                    Intent intent = new Intent(this, PostDetailActivity.class);
                    intent.putExtra("post_caption", post.getCaption());
                    intent.putExtra("post_image", post.getImageUrl());
                    startActivity(intent);
                }
        );

        binding.feedRecyclerView.setAdapter(feedAdapter);
        binding.feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadDummyData() {
        String[] dummyImages = {
                "https://picsum.photos/seed/1/800",
                "https://picsum.photos/seed/2/800",
                "https://picsum.photos/seed/3/800",
                "https://picsum.photos/seed/4/800",
                "https://picsum.photos/seed/5/800"
        };

        String[] dummyUsernames = {"john_doe", "jane_smith", "mike_wilson", "sara_parker", "alex_jones"};

        for (int i = 0; i < 10; i++) {
            Post post = new Post(
                    "post_" + i,
                    dummyImages[i % dummyImages.length],
                    "This is a beautiful post #" + (i + 1),
                    "user_" + i,
                    "https://picsum.photos/seed/profile_" + i + "/200",
                    dummyUsernames[i % dummyUsernames.length]
            );
            dummyPosts.add(post);
        }
        feedAdapter.notifyDataSetChanged();
    }
}