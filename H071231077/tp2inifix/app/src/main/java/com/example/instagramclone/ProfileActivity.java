package com.example.instagramclone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private ProfilePostsAdapter postsAdapter;
    private StoryHighlightsAdapter highlightsAdapter;
    private List<Post> profilePosts;
    private List<Story> storyHighlights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profilePosts = new ArrayList<>();
        storyHighlights = new ArrayList<>();

        setupToolbar();
        setupRecyclerViews();
        loadDummyData();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("AchmadRa1han");
        }
    }

    private void setupRecyclerViews() {
        // Setup Posts Grid
        postsAdapter = new ProfilePostsAdapter(post -> {
            Intent intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra("post_caption", post.getCaption());
            intent.putExtra("post_image", post.getImageUrl());
            startActivity(intent);
        });

        binding.postsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.postsRecyclerView.setAdapter(postsAdapter);

        // Setup Highlights
        highlightsAdapter = new StoryHighlightsAdapter(story ->
                Toast.makeText(this, "Story: " + story.getTitle(), Toast.LENGTH_SHORT).show()
        );

        binding.highlightsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.highlightsRecyclerView.setAdapter(highlightsAdapter);
    }

    private void loadDummyData() {
        // Load dummy posts
        for (int i = 0; i < 5; i++) {
            Post post = new Post(
                    "profile_post_" + i,
                    "https://picsum.photos/seed/profile_" + i + "/400",
                    "Profile post #" + (i + 1),
                    "AchmadRa1han",
                    "https://picsum.photos/seed/profile/200",
                    "AchmadRa1han"
            );
            profilePosts.add(post);
        }
        postsAdapter.submitList(profilePosts);

        // Load dummy highlights
        for (int i = 0; i < 7; i++) {
            Story story = new Story(
                    "highlight_" + i,
                    "https://picsum.photos/seed/highlight_" + i + "/200",
                    "Story " + i,
                    "AchmadRa1han"
            );
            storyHighlights.add(story);
        }
        highlightsAdapter.submitList(storyHighlights);
    }
}