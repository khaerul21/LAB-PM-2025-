package com.example.praktikum03.activites;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.praktikum03.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.praktikum03.R;
import com.example.praktikum03.models.Post;
import com.example.praktikum03.utils.DataSource;
import com.example.praktikum03.utils.ImageUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView imageViewBack, imageViewPost;
    private CircleImageView imageViewProfile;
    private TextView textViewUsername, textViewLikes, textViewComments, textViewShare, textViewCaption;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewProfile = findViewById(R.id.iv_profile);
        imageViewPost = findViewById(R.id.imageViewPosts);
        textViewUsername = findViewById(R.id.tv_username);
        textViewLikes = findViewById(R.id.tv_likes);
        textViewComments = findViewById(R.id.tv_comments);
        textViewShare = findViewById(R.id.tv_shares);
        textViewCaption = findViewById(R.id.tv_caption);

        int postId = getIntent().getIntExtra("POST_ID", 0);
        if (postId != 0) {
            // Cari di FeedPostList
            for (Post p : DataSource.getFeedPostList()) {
                if (p.getId() == postId) {
                    post = p;
                    break;
                }
            }

            // Kalau belum ketemu, cari di ProfilePostList
            if (post == null) {
                for (Post p : DataSource.getProfilePostList()) {
                    if (p.getId() == postId) {
                        post = p;
                        break;
                    }
                }
            }

            if (post != null) {
                // Set up the UI with post data
                setupPostDetail();
            }
        }

        // Back button click listener
        imageViewBack.setOnClickListener(v -> finish());
    }

    private void setupPostDetail() {
        // Set post image - Cek apakah ada gambar yang diupload
        if (ImageUtils.hasUploadedImage(post.getId())) {
            // Gunakan gambar yang diupload
            Bitmap uploadedImage = ImageUtils.getUploadedImage(post.getId());
            imageViewPost.setImageBitmap(uploadedImage);
        } else {
            // Gunakan gambar default dari resource
            imageViewPost.setImageResource(post.getImageResourceId());
        }

        // Set user profile image and username
        imageViewProfile.setImageResource(post.getUser().getProfileImageResourceId());
        textViewUsername.setText(post.getUser().getUsername());

        // Set likes and caption
        textViewLikes.setText(String.valueOf(post.getLikesCount()));
        textViewComments.setText(String.valueOf(post.getCommentsCount()));
        textViewShare.setText(String.valueOf(post.getSharesCount()));
        textViewCaption.setText(post.getCaption());
    }
}