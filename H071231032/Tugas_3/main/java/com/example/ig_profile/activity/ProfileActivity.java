package com.example.ig_profile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ig_profile.DataHolder;
import com.example.ig_profile.adapter.FeedAdapter;
import com.example.ig_profile.adapter.HighlightAdapter;
import com.example.ig_profile.R;
import com.example.ig_profile.models.FeedItem;
import com.example.ig_profile.models.HighlightItem;
import com.example.ig_profile.models.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.example.ig_profile.EXTRA_USERNAME";
    public static final String EXTRA_NAME = "com.example.ig_profile.EXTRA_NAME";
    public static final String EXTRA_BIO = "com.example.ig_profile.EXTRA_BIO";
    public static final String EXTRA_PROFILE_IMAGE_URI = "com.example.ig_profile.EXTRA_PROFILE_IMAGE_URI";

    private TextView tvUsername, tvName, tvBio, tvPosts, tvFollowers, tvFollowing;
    private ImageView ivProfileImage;
    private List<FeedItem> feedItems;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    private FeedAdapter feedAdapter;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        User parcelableUser = getIntent().getParcelableExtra("user", User.class);

        if (parcelableUser != null) {
            // Buka profil user lain
            currentUser = parcelableUser;
            feedItems = currentUser.getFeedItems();

            updateProfileUI();
            setupHighlightRecyclerView();
            setupFeedRecyclerView();
            setupUserVisitUI();
        } else {
            // Buka profil sendiri
            if (DataHolder.getInstance().getCurrentUser() == null) {
                initDummyData();
            } else {
                currentUser = DataHolder.getInstance().getCurrentUser();
            }
            updateProfileUI();
            setupHighlightRecyclerView();
            setupFeedRecyclerView();
            setupEditProfileLauncher();
            setupEditButton();
            setupNewPostButton();
            setupHome();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        feedAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        tvUsername = findViewById(R.id.username);
        ivProfileImage = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.name);
        tvBio = findViewById(R.id.bio);
        tvPosts = findViewById(R.id.posts_count);
        tvFollowers = findViewById(R.id.followers_count);
        tvFollowing = findViewById(R.id.following_count);
    }

    private void initDummyData() {
        // Gambar profil default
        Uri defaultProfileImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.ig_profile);

        List<Integer> highlightItems1 = new ArrayList<>();
        highlightItems1.add(R.drawable.feed1_copy);
        highlightItems1.add(R.drawable.highlight_3);

        List<Integer> highlightItems2 = new ArrayList<>();
        highlightItems2.add(R.drawable.highlight_4);

        List<Integer> highlightItems3 = new ArrayList<>();
        highlightItems3.add(R.drawable.highlight_5);

        List<Integer> highlightItems4 = new ArrayList<>();
        highlightItems4.add(R.drawable.highlight_6);

        List<Integer> highlightItems5 = new ArrayList<>();
        highlightItems5.add(R.drawable.highlight_7);

        List<Integer> highlightItems6 = new ArrayList<>();
        highlightItems6.add(R.drawable.highlight_2);

        List<Integer> highlightItems7 = new ArrayList<>();
        highlightItems7.add(R.drawable.highlight_b);

        List<HighlightItem> highlightList = new ArrayList<>();
        highlightList.add(new HighlightItem(R.drawable.feed1_copy, "A", highlightItems1));
        highlightList.add(new HighlightItem(R.drawable.highlight_4, "B", highlightItems2));
        highlightList.add(new HighlightItem(R.drawable.highlight_5, "C", highlightItems3));
        highlightList.add(new HighlightItem(R.drawable.highlight_6, "D", highlightItems4));
        highlightList.add(new HighlightItem(R.drawable.highlight_7, "E", highlightItems5));
        highlightList.add(new HighlightItem(R.drawable.highlight_2, "F", highlightItems6));
        highlightList.add(new HighlightItem(R.drawable.highlight_b, "G", highlightItems7));

        FeedItem feed1 = new FeedItem(R.drawable.feed1, "Sunset vibes", 120, 45, 10);
        FeedItem feed2 = new FeedItem(R.drawable.feed2, "Jogging time", 90, 30, 5);
        FeedItem feed3 = new FeedItem(R.drawable.feed3, "Morning brew", 150, 60, 15);
        FeedItem feed4 = new FeedItem(R.drawable.feed_guest4, "Eifellll", 180, 75, 20);
        FeedItem feed5 = new FeedItem(R.drawable.feed_guest4_2, "Good afternoon, friends!", 180, 75, 20);
        FeedItem feed6 = new FeedItem(R.drawable.feed_guest4_3, "Sunset in Paris", 180, 75, 20);

        feedItems = new ArrayList<>();
        feedItems.add(feed1);
        feedItems.add(feed2);
        feedItems.add(feed3);
        feedItems.add(feed4);
        feedItems.add(feed5);
        feedItems.add(feed6);
        feedItems.add(feed1);
        feedItems.add(feed2);
        feedItems.add(feed3);
        feedItems.add(feed4);
        feedItems.add(feed5);
        feedItems.add(feed6);

        currentUser = new User(
                defaultProfileImageUri.toString(),
                "_king_",
                "Kinggg",
                "Just a coding monarch.",
                1230,
                10200000,
                feedItems,
                highlightList
        );

        // Set Data Awal ke UI
        updateProfileUI();

        // Simpan data global
        DataHolder.getInstance().setCurrentUser(currentUser);
    }

    private void updateProfileUI() {
        int posts = currentUser.getPosts();
        int followers = currentUser.getFollowers();
        int following = currentUser.getFollowing();

        tvUsername.setText(currentUser.getUsername());
        tvName.setText(currentUser.getName());
        tvBio.setText(currentUser.getBio());
        ivProfileImage.setImageURI(Uri.parse(currentUser.getProfileImageUriString()));

        tvPosts.setText(formatNumber(posts));
        tvFollowers.setText(formatNumber(followers));
        tvFollowing.setText(formatNumber(following));
    }

    private void setupHome() {
        ImageView homeIcon = findViewById(R.id.home_activity);
        homeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        });

    }

    private void setupHighlightRecyclerView() {
        RecyclerView highlightRecyclerView = findViewById(R.id.highlightRecyclerView);
        if (currentUser.getHighlightItems() == null) {
            highlightRecyclerView.setVisibility(View.GONE);
            return;
        }
        highlightRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        HighlightAdapter adapter;
        if (currentUser.getUsername().equals(DataHolder.getInstance().getCurrentUser().getUsername())) {
            adapter = new HighlightAdapter(currentUser.getHighlightItems(), this, true);
        } else {
            adapter = new HighlightAdapter(currentUser.getHighlightItems(), this, false);
        }
        highlightRecyclerView.setAdapter(adapter);
    }

    private void setupFeedRecyclerView() {
        RecyclerView feedView = findViewById(R.id.feed_view);
        feedView.setLayoutManager(new GridLayoutManager(this, 3));

        feedAdapter = new FeedAdapter(currentUser.getFeedItems(), currentUser, this, true);
        feedView.setAdapter(feedAdapter);
    }

    private void setupEditProfileLauncher() {
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String updatedUsername = data.getStringExtra(EXTRA_USERNAME);
                        String updatedName = data.getStringExtra(EXTRA_NAME);
                        String updatedBio = data.getStringExtra(EXTRA_BIO);
                        String newImageUriString = data.getStringExtra(EXTRA_PROFILE_IMAGE_URI);

                        if (updatedUsername != null) currentUser.setUsername(updatedUsername);
                        if (updatedName != null) currentUser.setName(updatedName);
                        if (updatedBio != null) currentUser.setBio(updatedBio);
                        if (newImageUriString != null && !newImageUriString.isEmpty()) {
                            currentUser.setProfileImageUriString(newImageUriString);
                        }

                        updateProfileUI();
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupEditButton() {
        Button editBtn = findViewById(R.id.edit_profile_btn);
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra(EXTRA_USERNAME, currentUser.getUsername());
            intent.putExtra(EXTRA_NAME, currentUser.getName());
            intent.putExtra(EXTRA_BIO, currentUser.getBio());
            intent.putExtra(EXTRA_PROFILE_IMAGE_URI, currentUser.getProfileImageUriString());
            editProfileLauncher.launch(intent);
        });
    }

    private void setupUserVisitUI() {
        Button editBtn = findViewById(R.id.edit_profile_btn);
        editBtn.setText("Followed");
        Button shareBtn = findViewById(R.id.share_btn);
        shareBtn.setText("Message");

        ImageView threadsIcon = findViewById(R.id.threads);
        threadsIcon.setVisibility(View.GONE);
        ImageView addIcon = findViewById(R.id.addIcon);
        addIcon.setVisibility(View.GONE);
        ImageView hamburgerIcon = findViewById(R.id.hamburgerIcon);
        hamburgerIcon.setVisibility(View.GONE);

        ImageView lockIcon = findViewById(R.id.lock);
        lockIcon.setImageResource(R.drawable.ic_back);
        lockIcon.setOnClickListener(v -> finish());

        Toolbar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setVisibility(View.GONE);

    }

    private void setupNewPostButton() {
        ImageView newPostBtn = findViewById(R.id.new_post_activity);
        newPostBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, NewPostActivity.class);
            startActivity(intent);
        });
    }

    private String formatNumber(int number) {
        if (number < 1_000) {
            return String.valueOf(number);
        } else if (number < 10_000) {
            // Format ribuan dengan koma, misalnya 1,000
            double ribuan = number;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.'); // titik sebagai pemisah ribuan
            DecimalFormat df = new DecimalFormat("#,###", symbols);
            return df.format(ribuan);
        } else if (number < 1_000_000) {
            // Format 10K–999K dengan koma, misalnya 12,4K
            double ribuan = number / 1_000.0;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');  // pakai koma
            DecimalFormat df = new DecimalFormat("0.#", symbols);
            return df.format(ribuan) + "K";
        } else if (number < 1_000_000_000) {
            // Format jutaan dengan koma, misalnya 2,3M
            double jutaan = number / 1_000_000.0;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("0.#", symbols);
            return df.format(jutaan) + "M";
        } else {
            // Format miliyaran dengan koma, misalnya 2,3B
            double miliyaran = number / 1_000_000_000.0;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("0.#", symbols);
            return df.format(miliyaran) + "B";
        }

    }

}