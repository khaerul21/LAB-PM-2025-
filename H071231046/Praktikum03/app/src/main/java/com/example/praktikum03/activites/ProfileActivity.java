package com.example.praktikum03.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum03.R;
import com.example.praktikum03.adapters.ProfilePostAdapter;
import com.example.praktikum03.adapters.StoryAdapter;
import com.example.praktikum03.models.User;
import com.example.praktikum03.utils.DataSource;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imageViewProfile;
    private TextView textViewUsername, textViewProfileName, textViewBio;
    private TextView textViewPostsCount, textViewFollowersCount, textViewFollowingCount;
    private RecyclerView recyclerViewStories, recyclerViewProfilePosts;
    private StoryAdapter storyAdapter;
    private ProfilePostAdapter profilePostAdapter;
    private ImageView ic_home, ic_post;
    private Button buttonFollowOrEdit, buttonMessageOrShare;
    private User user;
    private CircleImageView ic_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageViewProfile = findViewById(R.id.iv_profile);
        textViewUsername = findViewById(R.id.tv_username);
        textViewProfileName = findViewById(R.id.namaprofile);
        textViewBio = findViewById(R.id.bioprofile);
        textViewPostsCount = findViewById(R.id.textViewPostsCount);
        textViewFollowersCount = findViewById(R.id.textViewFollowersCount);
        textViewFollowingCount = findViewById(R.id.textViewFollowingCount);
        recyclerViewStories = findViewById(R.id.recyclerViewStories);
        recyclerViewProfilePosts = findViewById(R.id.recyclerViewProfilePosts);
        ic_home = findViewById(R.id.ic_home);
        ic_post = findViewById(R.id.ic_post);
        ic_profile = findViewById(R.id.ic_profile);

        int userId = getIntent().getIntExtra("USER_ID", 0);
        if (userId != 0) {
            for (User u : DataSource.getUserList()) {
                if (u.getId() == userId) {
                    user = u;
                    break;
                }
            }
        } else {
            user = DataSource.getCurrentUser();
        }

        if (user == null) {
            user = DataSource.getCurrentUser();
        }

        setupUserProfile();
        if (user.getId() == DataSource.getCurrentUser().getId()) {
            setupStoryHighlights();
        } else {
            recyclerViewStories.setVisibility(View.GONE);
        }

        setupStoryHighlights();

        setupProfilePosts();

        ic_home.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        ic_post.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddPostActivity.class);
            startActivity(intent);
        });

        ic_profile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupUserProfile() {
        imageViewProfile.setImageResource(user.getProfileImageResourceId());
        textViewUsername.setText(user.getUsername());
        textViewProfileName.setText(user.getProfileName());
        textViewBio.setText(user.getBio());
        textViewPostsCount.setText(String.valueOf(user.getPostsCount()));
        textViewFollowersCount.setText(String.valueOf(user.getFollowersCount()));
        textViewFollowingCount.setText(String.valueOf(user.getFollowingCount()));
        buttonFollowOrEdit = findViewById(R.id.buttonFollowOrEdit);
        buttonMessageOrShare = findViewById(R.id.buttonMessageOrShare);


        if (user.getId() == DataSource.getCurrentUser().getId()) {
            buttonFollowOrEdit.setText("Edit Profile");
            buttonMessageOrShare.setText("Share Profile");
        } else {
            buttonFollowOrEdit.setText("Following");
            buttonMessageOrShare.setText("Message");
        }

    }

    private void setupStoryHighlights() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewStories.setLayoutManager(layoutManager);

        storyAdapter = new StoryAdapter(this, DataSource.getStoryList());
        recyclerViewStories.setAdapter(storyAdapter);
    }

    private void setupProfilePosts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerViewProfilePosts.setLayoutManager(gridLayoutManager);
        profilePostAdapter = new ProfilePostAdapter(this, DataSource.getPostsByUser(user));
        recyclerViewProfilePosts.setAdapter(profilePostAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUserProfile();
        profilePostAdapter.notifyDataSetChanged();
    }
}