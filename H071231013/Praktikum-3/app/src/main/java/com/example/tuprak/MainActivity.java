package com.example.tuprak;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.adapters.PostAdapter;
import com.example.tuprak.adapters.StoryAdapter;
import com.example.tuprak.adapters.UserPostAdapter;
import com.example.tuprak.models.Post;
import com.example.tuprak.models.Story;
import com.example.tuprak.models.User;
import com.example.tuprak.models.UserPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private RecyclerView storiesRecyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PostManager postManager = new PostManager(this);
        postManager.migrateExistingPosts();

        InitializationManager initManager = new InitializationManager(this);
        if (!initManager.isInitialized()) {
            initManager.initialize();
        }

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        storiesRecyclerView = findViewById(R.id.storiesRecyclerView);
        LinearLayoutManager storiesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        storiesRecyclerView.setLayoutManager(storiesLayoutManager);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        createDummyStories();

        List<UserPost> userPosts = postManager.getUserPosts();

        postsRecyclerView.setAdapter(new UserPostAdapter(this, userPosts));

        storyAdapter = new StoryAdapter(this, storyList);
        storiesRecyclerView.setAdapter(storyAdapter);

        setupBottomNavigation();
    }

    private void createDummyStories() {
        storyList = new ArrayList<>();

        // Get user profile image
        ProfilePreferencesManager preferencesManager = new ProfilePreferencesManager(this);
        Uri profileImageUri = preferencesManager.getProfileImageUri();
        
        // Create your story with proper flag to indicate it uses a URI instead of resource
        Story userStory;
        if (profileImageUri != null) {
            // Create story with custom flag and store URI as string in a new property
            userStory = new Story("Your Story", R.drawable.lab_logo, false, true, false);
            userStory.setCustomProfileImageUri(profileImageUri.toString());
        } else {
            // Fallback to default image if no profile image is set
            userStory = new Story("Your Story", R.drawable.lab_logo, false, true, false);
        }
        
        storyList.add(userStory);
        storyList.add(new Story("test_user", R.drawable.lab_logo, true, false, true));
    }

    private void setupBottomNavigation() {
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navSearch = findViewById(R.id.navSearch);
        ImageView navAdd = findViewById(R.id.navAdd);
        ImageView navReels = findViewById(R.id.navReels);
        ImageView navProfile = findViewById(R.id.navProfile);

        navHome.setImageResource(R.drawable.ic_home);
        navSearch.setImageResource(R.drawable.ic_search);
        navAdd.setImageResource(R.drawable.ic_add_post);
        navReels.setImageResource(R.drawable.ic_reels);

        // Load profile picture from SharedPreferences
        ProfilePreferencesManager preferencesManager = new ProfilePreferencesManager(this);
        Uri profileImageUri = preferencesManager.getProfileImageUri();

        if (profileImageUri != null) {
            try {
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);

                int iconSize = (int) getResources().getDimension(R.dimen.nav_icon_size);
                if (iconSize == 0)
                    iconSize = 24; 

                float density = getResources().getDisplayMetrics().density;
                int sizeInPixels = (int) (iconSize * density);

                int actualImageSize = (int) (sizeInPixels * 0.85); // 85% of the icon size

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap,
                        actualImageSize, actualImageSize, true);

                Bitmap circularBitmap = Bitmap.createBitmap(actualImageSize, actualImageSize,
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(circularBitmap);
                Paint paint = new Paint();
                paint.setAntiAlias(true);

                canvas.drawCircle(actualImageSize / 2f, actualImageSize / 2f, actualImageSize / 2f, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                canvas.drawBitmap(scaledBitmap, 0, 0, paint);

                navProfile.setImageBitmap(circularBitmap);

                navProfile.setPadding(12, 12, 12, 12);
                navProfile.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                navProfile.setBackground(null);

            } catch (Exception e) {
                navProfile.setImageResource(R.drawable.ic_profile);
                navProfile.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive));
            }
        } else {
            navProfile.setImageResource(R.drawable.ic_profile);
            navProfile.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive));
        }

        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        PostManager postManager = new PostManager(this);
        List<UserPost> userPosts = postManager.getUserPosts();
        
        if (postsRecyclerView.getAdapter() instanceof UserPostAdapter) {
            UserPostAdapter adapter = (UserPostAdapter) postsRecyclerView.getAdapter();
            adapter.updatePosts(userPosts);
        }
        
        if (storyAdapter != null) {
            storyAdapter.notifyDataSetChanged();
        }
    }
}