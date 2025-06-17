package com.example.onstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView username, postCount, followersCount, followingCount, bio;
    private RecyclerView feedRecyclerView;
    private PostAdapter postAdapter;
    private Button backBtn, addBtn;

    private User user; // ⬅ Tambahkan ini agar bisa diakses global
    private ArrayList<Feed> userFeeds; // ⬅ Tambahkan ini agar tidak error

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);

        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        postCount = findViewById(R.id.post_count);
        followersCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        bio = findViewById(R.id.bio);

        user = getIntent().getParcelableExtra("user");
        userFeeds = getIntent().getParcelableArrayListExtra("userFeeds");


        if (user != null) {
            username.setText(user.getUsername());
            postCount.setText(String.valueOf(user.getPostCount()));
            followersCount.setText(String.valueOf(user.getFollowers()));
            followingCount.setText(String.valueOf(user.getFollowing()));

            Glide.with(this)
                    .load(user.getProfileImageUrl())
                    .circleCrop()
                    .into(profileImage);

            bio.setText("Hello, I'm " + user.getUsername());

            logUserInfo(user);

            feedRecyclerView = findViewById(R.id.post_grid);
            feedRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            postAdapter = new PostAdapter(this, userFeeds);
            feedRecyclerView.setAdapter(postAdapter);
        }

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });



        addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, AddFeed.class);
            intent.putExtra("user", user);
            intent.putParcelableArrayListExtra("userFeeds", userFeeds);
            startActivityForResult(intent, 100);
        });


    }
    private void logUserInfo(User user) {
        try {
            StringBuilder logMessage = new StringBuilder(user.getClass().getSimpleName() + " = {");
            Field[] fields = user.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                logMessage.append(field.getName()).append("=").append(field.get(user)).append(", ");
            }
            logMessage.append("}");
            Log.d(user.getClass().getSimpleName(), logMessage.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Feed newFeed = data.getParcelableExtra("newFeed");
            User updatedUser = data.getParcelableExtra("user");

            if (newFeed != null && updatedUser != null) {
                userFeeds.add(0, newFeed);
                postAdapter.notifyItemInserted(0);
                feedRecyclerView.scrollToPosition(0);
                postCount.setText(String.valueOf(updatedUser.getPostCount()));
            }
        }
    }
}
