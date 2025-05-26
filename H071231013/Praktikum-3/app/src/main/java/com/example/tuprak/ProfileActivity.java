package com.example.tuprak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.adapters.PostAdapter;
import com.example.tuprak.adapters.ProfileGridAdapter;
import com.example.tuprak.models.Post;
import com.example.tuprak.models.User;
import com.example.tuprak.models.UserPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final String[] REQUIRED_PERMISSIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ? new String[] { Manifest.permission.READ_MEDIA_IMAGES }
            : new String[] { Manifest.permission.READ_EXTERNAL_STORAGE };

    private TextView profileNameText;
    private TextView usernameText;
    private TextView bioText1;
    private TextView bioText2;
    private TextView bioText3;
    private TextView websiteText;
    private Button messageButton;
    private Button followButton;
    private ImageView profilePic;
    private Uri profileImageUri;
    private TextView postsCount, followersCount, followingCount;

    private RecyclerView postsGridRecyclerView;
    private ProfileGridAdapter gridAdapter;
    private PostManager postManager;
    private BroadcastReceiver postUpdateReceiver;
    private View emptyStateView;

    private String userGender = "Laki-laki";
    private String userKataGanti = "";
    private ProfilePreferencesManager preferencesManager;

    private boolean isViewingOtherUser = false;
    private String viewingUsername = null;

    private UserManager userManager;
    private User currentViewingUser;

    private PostAdapter feedAdapter;

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Intent data = result.getData();
                    String name = data.getStringExtra("name");
                    String username = data.getStringExtra("username");
                    String bio = data.getStringExtra("bio");
                    String links = data.getStringExtra("links");

                    userGender = data.getStringExtra("gender");
                    userKataGanti = data.getStringExtra("kataganti");

                    preferencesManager.saveGender(userGender);
                    preferencesManager.saveKataGanti(userKataGanti);

                    if (data.hasExtra("profileImageUri")) {
                        String imageUriString = data.getStringExtra("profileImageUri");
                        if (imageUriString != null && !imageUriString.isEmpty()) {
                            Uri receivedUri = Uri.parse(imageUriString);
                            try {
                                profileImageUri = createPersistentUri(receivedUri);
                                profilePic.setImageURI(profileImageUri);
                                preferencesManager.saveProfileImageUri(profileImageUri);

                                Intent updateProfileIntent = new Intent("com.example.tuprak.PROFILE_IMAGE_UPDATED");
                                updateProfileIntent.putExtra("profile_image_uri", profileImageUri.toString());
                                sendBroadcast(updateProfileIntent);
                            } catch (SecurityException e) {
                                Log.e(TAG, "Permission denied accessing image URI: " + receivedUri, e);
                                Toast.makeText(this, "Permission denied to access image", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Error loading image from URI: " + receivedUri, e);
                            }
                        }
                    }

                    if (name != null && !name.isEmpty()) {
                        profileNameText.setText(name);
                        preferencesManager.saveProfileName(name);
                    }

                    if (username != null && !username.isEmpty()) {
                        usernameText.setText(username);
                        preferencesManager.saveUsername(username);
                    }

                    if (bio != null) {
                        String[] bioLines = bio.split("\n");

                        bioText1.setText("");
                        bioText2.setText("");
                        bioText3.setText("");

                        if (bioLines.length > 0) {
                            bioText1.setText(bioLines[0]);
                            preferencesManager.saveBioLine1(bioLines[0]);

                            if (bioLines.length > 1) {
                                bioText2.setText(bioLines[1]);
                                preferencesManager.saveBioLine2(bioLines[1]);

                                if (bioLines.length > 2) {
                                    bioText3.setText(bioLines[2]);
                                    preferencesManager.saveBioLine3(bioLines[2]);
                                } else {
                                    preferencesManager.saveBioLine3("");
                                }
                            } else {
                                preferencesManager.saveBioLine2("");
                                preferencesManager.saveBioLine3("");
                            }
                        } else {
                            preferencesManager.saveBioLine1("");
                            preferencesManager.saveBioLine2("");
                            preferencesManager.saveBioLine3("");
                        }
                    }

                    if (links != null && !links.isEmpty()) {
                        websiteText.setText(links);
                        preferencesManager.saveWebsite(links);
                    }
                    UserManager userManager = new UserManager(this);
                    User currentUser = userManager.getUserByUsername(username);

                    if (currentUser == null) {
                        currentUser = new User(username, name);
                    } else {
                        currentUser.setFullName(name);
                    }

                    currentUser.setBioLine1(bioText1.getText().toString());
                    currentUser.setBioLine2(bioText2.getText().toString());
                    currentUser.setBioLine3(bioText3.getText().toString());
                    currentUser.setWebsite(websiteText.getText().toString());

                    if (profileImageUri != null) {
                        currentUser.setProfileImageUri(profileImageUri.toString());
                    }

                    userManager.updateUser(currentUser);

                    userManager.setCurrentUser(username);

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        userManager = new UserManager(this);
        postManager = new PostManager(this);
        preferencesManager = new ProfilePreferencesManager(this);

        if (getIntent().hasExtra("username")) {
            viewingUsername = getIntent().getStringExtra("username");

            User currentUser = userManager.getCurrentUser();
            isViewingOtherUser = !viewingUsername.equals(currentUser.getUsername());
        }

        if (isViewingOtherUser) {
            setContentView(R.layout.activity_other_user_profile);
        } else {
            setContentView(R.layout.activity_profile);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            View scrollView = findViewById(R.id.scrollView);
            scrollView.setPadding(scrollView.getPaddingLeft(),
                    systemBars.top,
                    scrollView.getPaddingRight(),
                    scrollView.getPaddingBottom());

            return WindowInsetsCompat.CONSUMED;
        });

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        initializeViews();

        checkAndRequestPermissions();

        loadProfileData();

        setupActionButtons();

        setupPostsGrid();

        if (!isViewingOtherUser && findViewById(R.id.sbdHighlight) != null) {
            setupStoryHighlights();
        }

        setupBottomNavigation();
        postUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshPosts();
            }
        };
        registerReceiver(postUpdateReceiver,
                new IntentFilter("com.example.tuprak.NEW_POST_CREATED"),
                Context.RECEIVER_NOT_EXPORTED);

        setupTabsNavigation();
    }

    private void initializeViews() {
        profileNameText = findViewById(R.id.profileNameText);
        usernameText = findViewById(R.id.usernameText);
        bioText1 = findViewById(R.id.bioText1);
        bioText2 = findViewById(R.id.bioText2);
        bioText3 = findViewById(R.id.bioText3);
        websiteText = findViewById(R.id.websiteText);
        profilePic = findViewById(R.id.profilePic);
        postsGridRecyclerView = findViewById(R.id.postsGridRecyclerView);
        emptyStateView = findViewById(R.id.emptyStateView);

        postsCount = findViewById(R.id.postsCount);
        followersCount = findViewById(R.id.followersCount);
        followingCount = findViewById(R.id.followingCount);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        if (isViewingOtherUser) {
            messageButton = findViewById(R.id.messageButton);
            followButton = findViewById(R.id.followButton);
        } else {
            messageButton = findViewById(R.id.messageButton);
        }
    }

    private void setupActionButtons() {
        if (isViewingOtherUser) {
            followButton.setOnClickListener(v -> {
                boolean isFollowing = followButton.getText().toString().equals("Following");
                if (isFollowing) {
                    followButton.setText("Follow");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(
                            getResources().getColor(R.color.instagram_blue, getTheme())));

                    int followers = Integer.parseInt(followersCount.getText().toString());
                    if (followers > 0) {
                        followers--;
                        followersCount.setText(String.valueOf(followers));
                    }

                    userManager.toggleFollow(userManager.getCurrentUser().getUsername(), viewingUsername);
                } else {
                    followButton.setText("Following");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(
                            getResources().getColor(R.color.dark_gray, getTheme())));

                    int followers = Integer.parseInt(followersCount.getText().toString());
                    followers++;
                    followersCount.setText(String.valueOf(followers));

                    userManager.toggleFollow(userManager.getCurrentUser().getUsername(), viewingUsername);
                }
            });

        } else {
            messageButton.setText("Edit profil");
            messageButton.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("name", profileNameText.getText().toString());
                intent.putExtra("username", usernameText.getText().toString());

                StringBuilder bioBuilder = new StringBuilder();
                if (bioText1 != null)
                    bioBuilder.append(bioText1.getText());
                bioBuilder.append("\n");
                if (bioText2 != null)
                    bioBuilder.append(bioText2.getText());
                bioBuilder.append("\n");
                if (bioText3 != null)
                    bioBuilder.append(bioText3.getText());

                intent.putExtra("bio", bioBuilder.toString());
                intent.putExtra("links", websiteText.getText().toString());
                intent.putExtra("gender", userGender);
                intent.putExtra("kataganti", userKataGanti);

                if (profileImageUri != null) {
                    intent.putExtra("profileImageUri", profileImageUri.toString());
                }
                editProfileLauncher.launch(intent);
            });
        }
    }

    private void loadProfileData() {
        if (isViewingOtherUser && viewingUsername != null) {
            currentViewingUser = userManager.getUserByUsername(viewingUsername);

            if (currentViewingUser != null) {
                usernameText.setText(currentViewingUser.getUsername());
                profileNameText.setText(currentViewingUser.getFullName());
                bioText1.setText(currentViewingUser.getBioLine1() != null ? currentViewingUser.getBioLine1() : "");
                bioText2.setText(currentViewingUser.getBioLine2() != null ? currentViewingUser.getBioLine2() : "");
                bioText3.setText(currentViewingUser.getBioLine3() != null ? currentViewingUser.getBioLine3() : "");
                if (currentViewingUser.getWebsite() != null && !currentViewingUser.getWebsite().isEmpty()) {
                    websiteText.setText(currentViewingUser.getWebsite());
                    websiteText.setOnClickListener(v -> {
                        String url = currentViewingUser.getWebsite();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    });
                } else {
                    websiteText.setText("");
                    websiteText.setOnClickListener(null);
                }

                if (currentViewingUser.getProfileImageUri() != null
                        && !currentViewingUser.getProfileImageUri().isEmpty()) {
                    try {
                        Uri profileUri = Uri.parse(currentViewingUser.getProfileImageUri());
                        if (profileUri != null) {
                            if (profileUri.toString().contains("android.resource")) {
                                String uriStr = profileUri.toString();
                                String[] parts = uriStr.split("/");
                                String resourceName = parts[parts.length - 1];
                                
                                int resourceId = getResources().getIdentifier(
                                    resourceName, "drawable", getPackageName());
                                    
                                if (resourceId != 0) {
                                    profilePic.setImageResource(resourceId);
                                    Log.d(TAG, "Successfully loaded profile image resource: " + resourceName);
                                } else {
                                    profilePic.setImageResource(R.drawable.lab_logo);
                                    Log.e(TAG, "Resource not found: " + resourceName);
                                }
                            } else {
                                profilePic.setImageURI(profileUri);
                            }
                        } else {
                            profilePic.setImageResource(R.drawable.lab_logo);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading profile image", e);
                        profilePic.setImageResource(R.drawable.lab_logo);
                    }
                } else {
                    profilePic.setImageResource(R.drawable.lab_logo);
                }

                if (postsCount != null) {
                    postsCount.setText(String.valueOf(currentViewingUser.getPostsCount()));
                }
                if (followersCount != null) {
                    followersCount.setText(String.valueOf(currentViewingUser.getFollowersCount()));
                }
                if (followingCount != null) {
                    followingCount.setText(String.valueOf(currentViewingUser.getFollowingCount()));
                }

                if (followButton != null) {
                    boolean isFollowing = userManager.isFollowing(
                            userManager.getCurrentUser().getUsername(),
                            currentViewingUser.getUsername());

                    followButton.setText(isFollowing ? "Following" : "Follow");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(
                            isFollowing ? R.color.dark_gray : R.color.instagram_blue, getTheme())));
                }

                setupUserStoryHighlights(currentViewingUser);

                ImageView verifiedBadge = findViewById(R.id.verifiedBadge);
                if (verifiedBadge != null) {
                    verifiedBadge.setVisibility(currentViewingUser.isVerified() ? View.VISIBLE : View.GONE);
                }
            }
        } else {
            String savedName = preferencesManager.getProfileName("");
            if (!savedName.isEmpty()) {
                profileNameText.setText(savedName);
            }

            String savedUsername = preferencesManager.getUsername("");
            if (!savedUsername.isEmpty()) {
                usernameText.setText(savedUsername);
            }

            bioText1.setText(preferencesManager.getBioLine1(""));
            bioText2.setText(preferencesManager.getBioLine2(""));
            bioText3.setText(preferencesManager.getBioLine3(""));

            String savedWebsite = preferencesManager.getWebsite("");
            if (!savedWebsite.isEmpty()) {
                websiteText.setText(savedWebsite);
            }

            userGender = preferencesManager.getGender("Laki-laki");
            userKataGanti = preferencesManager.getKataGanti("");

            profileImageUri = preferencesManager.getProfileImageUri();
            if (profileImageUri != null) {
                try {
                    profilePic.setImageURI(profileImageUri);
                } catch (SecurityException e) {
                    Log.e(TAG, "Permission denied accessing image URI: " + profileImageUri, e);
                    profilePic.setImageResource(R.drawable.lab_logo);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading image from URI: " + profileImageUri, e);
                    profilePic.setImageResource(R.drawable.lab_logo);
                }
            }

            User currentUser = userManager.getCurrentUser();
            if (postsCount != null) {
                postsCount.setText(String.valueOf(currentUser.getPostsCount()));
            }
            if (followersCount != null) {
                followersCount.setText(String.valueOf(currentUser.getFollowersCount()));
            }
            if (followingCount != null) {
                followingCount.setText(String.valueOf(currentUser.getFollowingCount()));
            }
        }
    }

    private void setupPostsGrid() {
        if (postsGridRecyclerView != null) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            postsGridRecyclerView.setLayoutManager(layoutManager);
            postsGridRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                        @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    outRect.left = 1;
                    outRect.right = 1;
                    outRect.bottom = 1;
                    outRect.top = 1;
                }
            });
    
            List<UserPost> posts;
            String targetUsername;
            String targetUserId = null;
            
            if (isViewingOtherUser) {
                targetUsername = viewingUsername;
                User viewingUser = userManager.getUserByUsername(targetUsername);
                if (viewingUser != null) {
                    targetUserId = viewingUser.getId();
                }
            } else {
                User currentUser = userManager.getCurrentUser();
                targetUsername = currentUser.getUsername();
                targetUserId = currentUser.getId();
            }
            
            if (targetUserId != null) {
                posts = postManager.getPostsByUserId(targetUserId);
            } else {
                posts = postManager.getPostsByUsername(targetUsername);
            }
            
    
            gridAdapter = new ProfileGridAdapter(this, posts);
            postsGridRecyclerView.setAdapter(gridAdapter);
    
            if (emptyStateView != null) {
                if (posts.isEmpty()) {
                    emptyStateView.setVisibility(View.VISIBLE);
                    postsGridRecyclerView.setVisibility(View.GONE);
                    Button createFirstPostButton = findViewById(R.id.createFirstPostButton);
                    if (createFirstPostButton != null) {
                        createFirstPostButton.setVisibility(isViewingOtherUser ? View.GONE : View.VISIBLE);
                    }
                } else {
                    emptyStateView.setVisibility(View.GONE);
                    postsGridRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            Log.e(TAG, "postsGridRecyclerView is null! Check your layout file.");
        }
    }

    private void refreshPosts() {
        if (gridAdapter != null && postsGridRecyclerView != null) {
            List<UserPost> userPosts;
            
            if (isViewingOtherUser) {
                String userId = null;
                User viewingUser = userManager.getUserByUsername(viewingUsername);
                if (viewingUser != null) {
                    userId = viewingUser.getId();
                    userPosts = postManager.getPostsByUserId(userId);
                } else {
                    userPosts = postManager.getPostsByUsername(viewingUsername);
                }
            } else {
                User currentUser = userManager.getCurrentUser();
                String userId = currentUser.getId();
                userPosts = (userId != null) ? 
                        postManager.getPostsByUserId(userId) : 
                        postManager.getPostsByUsername(currentUser.getUsername());
            }
    
            gridAdapter.updatePosts(userPosts);
    
            if (emptyStateView != null) {
                if (userPosts.isEmpty()) {
                    emptyStateView.setVisibility(View.VISIBLE);
                    postsGridRecyclerView.setVisibility(View.GONE);
                    Button createFirstPostButton = findViewById(R.id.createFirstPostButton);
                    if (createFirstPostButton != null) {
                        createFirstPostButton.setVisibility(isViewingOtherUser ? View.GONE : View.VISIBLE);
                    }
                } else {
                    emptyStateView.setVisibility(View.GONE);
                    postsGridRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPosts();
        setupBottomNavigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (postUpdateReceiver != null) {
            try {
                unregisterReceiver(postUpdateReceiver);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error unregistering receiver", e);
            }
        }
    }

    private Uri createPersistentUri(Uri sourceUri) {
        try {
            File outputDir = new File(getFilesDir(), "profile_images");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String fileName = "profile_image_" + System.currentTimeMillis() + ".jpg";
            File outputFile = new File(outputDir, fileName);

            try (InputStream inputStream = getContentResolver().openInputStream(sourceUri);
                    OutputStream outputStream = new FileOutputStream(outputFile)) {

                if (inputStream == null) {
                    throw new IOException("Failed to open input stream");
                }

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();

                return FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        outputFile);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to create persistent URI", e);

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(sourceUri));

                File outputDir = new File(getFilesDir(), "profile_images");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }

                String fileName = "profile_image_" + System.currentTimeMillis() + ".jpg";
                File outputFile = new File(outputDir, fileName);

                FileOutputStream fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();

                return FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        outputFile);
            } catch (Exception e2) {
                Log.e(TAG, "Failed backup attempt to create persistent URI", e2);
                return null;
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        boolean allGranted = true;
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }
        if (!allGranted) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                loadProfileData();
            } else {
                Toast.makeText(this, "Storage permissions are required to load profile image",
                        Toast.LENGTH_LONG).show();
            }
        }
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
                int actualImageSize = (int) (sizeInPixels * 0.85);

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

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, NewPostActivity.class);
            startActivity(intent);
        });
    }

    private void setupStoryHighlights() {
        LinearLayout sbdHighlight = findViewById(R.id.sbdHighlight);
        LinearLayout pboHighlight = findViewById(R.id.pboHighlight);
        LinearLayout webHighlight = findViewById(R.id.webHighlight);
        LinearLayout apHighlight = findViewById(R.id.apHighlight);
        LinearLayout mobileDevHighlight = findViewById(R.id.mobileDevHighlight);
        LinearLayout dssHighlight = findViewById(R.id.dssHighlight);     // New highlight
        LinearLayout dbHighlight = findViewById(R.id.dbHighlight);       // New highlight
    
        if (sbdHighlight != null) {
            sbdHighlight.setOnClickListener(v -> openStoryViewer("SBD 2024", R.drawable.highlight_1));
        }
    
        if (pboHighlight != null) {
            pboHighlight.setOnClickListener(v -> openStoryViewer("PBO 2024", R.drawable.highlight_2));
        }
    
        if (webHighlight != null) {
            webHighlight.setOnClickListener(v -> openStoryViewer("WEB 2024", R.drawable.highlight_3));
        }
    
        if (apHighlight != null) {
            apHighlight.setOnClickListener(v -> openStoryViewer("AP 2024", R.drawable.highlight_4));
        }
    
        if (mobileDevHighlight != null) {
            mobileDevHighlight.setOnClickListener(v -> openStoryViewer("Mobile Dev", R.drawable.highlight_5));
        }
        
        // Add click handlers for new highlights
        if (dssHighlight != null) {
            dssHighlight.setOnClickListener(v -> openStoryViewer("DSS 2024", R.drawable.highlight_1));
        }
        
        if (dbHighlight != null) {
            dbHighlight.setOnClickListener(v -> openStoryViewer("Database", R.drawable.highlight_3));
        }
    }

    private void openStoryViewer(String title, int imageResId) {
        Intent intent = new Intent(this, StoryViewerActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("image_resource", imageResId);
        intent.putExtra("timestamp", "2 HOURS AGO");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
    }

    private void setupUserStoryHighlights(User user) {
        LinearLayout highlightsSection = findViewById(R.id.highlightsSection);
        if (highlightsSection == null)
            return;

        highlightsSection.removeAllViews();

        String[] highlightTopics = generateHighlightTopicsForUser(user.getUsername());

        for (String topic : highlightTopics) {
            View highlightView = getLayoutInflater().inflate(
                    R.layout.item_story_highlight, highlightsSection, false);

            CircleImageView highlightImage = highlightView.findViewById(R.id.highlightImage);
            TextView highlightText = highlightView.findViewById(R.id.highlightText);

            int[] highlightImages = {
                    R.drawable.ic_launcher_background,
                    R.drawable.ic_bangrang,
                    R.drawable.lab_logo
            };

            Random random = new Random();
            highlightImage.setImageResource(highlightImages[random.nextInt(highlightImages.length)]);
            highlightText.setText(topic);

            highlightView.setOnClickListener(v -> openStoryViewer(topic, highlightImages[0]));

            highlightsSection.addView(highlightView);
        }
    }

    private String[] generateHighlightTopicsForUser(String username) {
        if (username.contains("web")) {
            return new String[] { "HTML5", "CSS3", "JS", "React", "Angular", "Node", "Django" };
        } else if (username.contains("mobile")) {
            return new String[] { "Android", "iOS", "Flutter", "React Native", "Kotlin", "Swift", "UI/UX" };
        } else if (username.contains("pbo")) {
            return new String[] { "Java", "Python", "C#", "OOP", "Patterns", "Classes", "Inheritance" };
        } else if (username.contains("design")) {
            return new String[] { "UI", "UX", "Color", "Typography", "Layout", "Figma", "Adobe XD" };
        } else if (username.contains("data")) {
            return new String[] { "Python", "R", "SQL", "ML", "Visualization", "Big Data", "Statistics" };
        }

        return new String[] { "Posts", "Travel", "Food", "Fitness", "Work", "Favorites", "Memories" };
    }

    private void setupTabsNavigation() {
        View gridTab = findViewById(R.id.gridTab);
        View feedTab = findViewById(R.id.feedTab);
        View taggedTab = findViewById(R.id.taggedTab);
        
        if (gridTab == null || feedTab == null || taggedTab == null) {
            Log.d(TAG, "Tab navigation elements not found in current layout");
            return;
        }
        
        ImageView gridTabIcon = findViewById(R.id.gridTabIcon);
        ImageView feedTabIcon = findViewById(R.id.feedTabIcon);
        ImageView taggedTabIcon = findViewById(R.id.taggedTabIcon);
        
        View gridTabIndicator = findViewById(R.id.gridTabIndicator);
        View feedTabIndicator = findViewById(R.id.feedTabIndicator);
        View taggedTabIndicator = findViewById(R.id.taggedTabIndicator);
        
        RecyclerView postsGridRecyclerView = findViewById(R.id.postsGridRecyclerView);
        RecyclerView postsFeedRecyclerView = findViewById(R.id.postsFeedRecyclerView);
        RecyclerView postsTaggedRecyclerView = findViewById(R.id.postsTaggedRecyclerView);
        
        gridTab.setOnClickListener(v -> {
            gridTabIndicator.setVisibility(View.VISIBLE);
            feedTabIndicator.setVisibility(View.INVISIBLE);
            taggedTabIndicator.setVisibility(View.INVISIBLE);
            
            gridTabIcon.setAlpha(1.0f);
            feedTabIcon.setAlpha(0.5f);
            taggedTabIcon.setAlpha(0.5f);
            
            postsGridRecyclerView.setVisibility(View.VISIBLE);
            postsFeedRecyclerView.setVisibility(View.GONE);
            postsTaggedRecyclerView.setVisibility(View.GONE);
            
            if (gridAdapter != null && gridAdapter.getItemCount() > 0) {
                findViewById(R.id.emptyStateView).setVisibility(View.GONE);
            } else {
                findViewById(R.id.emptyStateView).setVisibility(View.VISIBLE);
            }
        });
        
        feedTab.setOnClickListener(v -> {
            gridTabIndicator.setVisibility(View.INVISIBLE);
            feedTabIndicator.setVisibility(View.VISIBLE);
            taggedTabIndicator.setVisibility(View.INVISIBLE);
            
            gridTabIcon.setAlpha(0.5f);
            feedTabIcon.setAlpha(1.0f);
            taggedTabIcon.setAlpha(0.5f);
            
            postsGridRecyclerView.setVisibility(View.GONE);
            postsFeedRecyclerView.setVisibility(View.VISIBLE);
            postsTaggedRecyclerView.setVisibility(View.GONE);
            
            if (feedAdapter == null && currentViewingUser != null) {
                setupFeedRecyclerView(currentViewingUser);
            }
            
            if (feedAdapter != null && feedAdapter.getItemCount() > 0) {
                findViewById(R.id.emptyStateView).setVisibility(View.GONE);
            } else {
                findViewById(R.id.emptyStateView).setVisibility(View.VISIBLE);
            }
        });
        
        taggedTab.setOnClickListener(v -> {
            gridTabIndicator.setVisibility(View.INVISIBLE);
            feedTabIndicator.setVisibility(View.INVISIBLE);
            taggedTabIndicator.setVisibility(View.VISIBLE);
            
            gridTabIcon.setAlpha(0.5f);
            feedTabIcon.setAlpha(0.5f);
            taggedTabIcon.setAlpha(1.0f);
            
            postsGridRecyclerView.setVisibility(View.GONE);
            postsFeedRecyclerView.setVisibility(View.GONE);
            postsTaggedRecyclerView.setVisibility(View.VISIBLE);
            
            findViewById(R.id.emptyStateView).setVisibility(View.VISIBLE);
        });
    }

    private void setupFeedRecyclerView(User user) {
        RecyclerView postsFeedRecyclerView = findViewById(R.id.postsFeedRecyclerView);
        if (postsFeedRecyclerView == null) return;
        
        List<UserPost> userPosts = postManager.getPostsByUserId(user.getId());
        
        List<Post> feedPosts = new ArrayList<>();
        for (UserPost userPost : userPosts) {
            int profileImageResource = R.drawable.lab_logo;
            String postImageUriString = userPost.getPostImageUri();
            
            Post post = new Post(
                userPost.getUsername(),
                profileImageResource, 
                R.drawable.foto1,
                userPost.getCaption(),
                userPost.getLikesCount(),
                userPost.getCommentsCount(),
                userPost.getTimePosted()
            );
            feedPosts.add(post);
        }
        
        feedAdapter = new PostAdapter(this, feedPosts);
        postsFeedRecyclerView.setAdapter(feedAdapter);
    }
}