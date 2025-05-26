package com.example.tuprak;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuprak.models.UserPost;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";

    private PostManager postManager;
    private UserPost post;
    
    private ImageView profileImage;
    private TextView username;
    private ImageView postImage;
    private TextView caption;
    private TextView likesCount;
    private TextView commentsText;
    private TextView timePosted;
    private ImageView likeButton;
    private ImageView commentButton;
    private ImageView shareButton;
    private ImageView saveButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);
        
        String postId = getIntent().getStringExtra("post_id");
        if (postId == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        postManager = new PostManager(this);
        List<UserPost> allPosts = postManager.getUserPosts();
        
        for (UserPost currentPost : allPosts) {
            if (currentPost.getId().equals(postId)) {
                post = currentPost;
                break;
            }
        }
        
        if (post == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        profileImage = findViewById(R.id.postProfileImage);
        username = findViewById(R.id.postUsername);
        postImage = findViewById(R.id.postImage);
        caption = findViewById(R.id.postCaption);
        likesCount = findViewById(R.id.postLikesCount);
        commentsText = findViewById(R.id.postCommentsText);
        timePosted = findViewById(R.id.postTimeText);
        likeButton = findViewById(R.id.postLikeButton);
        commentButton = findViewById(R.id.postCommentButton);
        shareButton = findViewById(R.id.postShareButton);
        saveButton = findViewById(R.id.postSaveButton);
        backButton = findViewById(R.id.backButton);
        
        backButton.setOnClickListener(v -> finish());
        
        displayPost();
        
        likeButton.setTag(false);
        likeButton.setOnClickListener(v -> {
            boolean isLiked = (boolean) likeButton.getTag();
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_like);
                likeButton.setTag(false);
            } else {
                likeButton.setImageResource(R.drawable.ic_like_filled);
                likeButton.setTag(true);
            }
        });
    }
    
    private void displayPost() {
        username.setText(post.getUsername());
        caption.setText(post.getCaption());
        
        likesCount.setText(String.format("%d likes", post.getLikesCount()));
        timePosted.setText(post.getTimePosted());

        Log.d(TAG, "Post details - Username: " + post.getUsername());
        Log.d(TAG, "Post details - Profile image: " + post.getUserProfileImageUri());
        Log.d(TAG, "Post details - Post image: " + post.getPostImageUri());

        try {
            if (post.getPostImageUri() != null) {
                String uriStr = post.getPostImageUri();
                
                if (uriStr.startsWith("android.resource://")) {
                    Uri uri = Uri.parse(uriStr);
                    String lastSegment = uri.getLastPathSegment();
                    
                    if (lastSegment != null) {
                        if (lastSegment.matches("\\d+")) {
                            int resId = Integer.parseInt(lastSegment);
                            postImage.setImageResource(resId);
                        } else {
                            String resourceName = lastSegment;
                            if (resourceName.contains("/")) {
                                resourceName = resourceName.substring(resourceName.lastIndexOf("/") + 1);
                            }
                            
                            int resId = getResources().getIdentifier(
                                    resourceName, "drawable", getPackageName());
                            
                            if (resId != 0) {
                                postImage.setImageResource(resId);
                            } else {
                                postImage.setImageResource(R.drawable.ic_bangrang);
                            }
                        }
                    }
                } else if (uriStr.startsWith("file://")) {
                    Log.d(TAG, "Loading file URI: " + uriStr);
                    postImage.setImageURI(Uri.parse(uriStr));
                } else {
                    int resId = getResources().getIdentifier(
                            uriStr, "drawable", getPackageName());
                    if (resId != 0) {
                        postImage.setImageResource(resId);
                    } else {
                        Log.d(TAG, "Trying as generic URI: " + uriStr);
                        postImage.setImageURI(Uri.parse(uriStr));
                    }
                }
            } else {
                postImage.setImageResource(R.drawable.ic_bangrang);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting post image", e);
            postImage.setImageResource(R.drawable.ic_bangrang);
        }
        try {
            if (post.getUserProfileImageUri() != null) {
                String uriStr = post.getUserProfileImageUri();

                if (uriStr.startsWith("android.resource://")) {
                    Uri uri = Uri.parse(uriStr);
                    String lastSegment = uri.getLastPathSegment();

                    if (lastSegment != null) {
                        if (lastSegment.matches("\\d+")) {
                            int resId = Integer.parseInt(lastSegment);
                            profileImage.setImageResource(resId);
                        } else {
                            String resourceName = lastSegment;
                            if (resourceName.contains("/")) {
                                resourceName = resourceName.substring(resourceName.lastIndexOf("/") + 1);
                            }

                            int resId = getResources().getIdentifier(
                                    resourceName, "drawable", getPackageName());

                            if (resId != 0) {
                                profileImage.setImageResource(resId);
                            } else {
                                profileImage.setImageResource(R.drawable.lab_logo);
                            }
                        }
                    } else {
                        profileImage.setImageResource(R.drawable.lab_logo);
                    }
                } else if (uriStr.startsWith("file://")) {
                    Log.d(TAG, "Loading profile file URI: " + uriStr);
                    profileImage.setImageURI(Uri.parse(uriStr));
                } else {
                    int resId = getResources().getIdentifier(
                            uriStr, "drawable", getPackageName());
                            
                    if (resId != 0) {
                        profileImage.setImageResource(resId);
                    } else {
                        Log.d(TAG, "Trying profile as generic URI: " + uriStr);
                        profileImage.setImageURI(Uri.parse(uriStr));
                    }
                }
            } else {
                profileImage.setImageResource(R.drawable.lab_logo);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting profile image", e);
            profileImage.setImageResource(R.drawable.lab_logo);
        }

        if (post.getCommentsCount() > 0) {
            commentsText.setText(String.format("View all %d comments", post.getCommentsCount()));
            commentsText.setVisibility(View.VISIBLE);
        } else {
            commentsText.setVisibility(View.GONE);
        }
    }
}