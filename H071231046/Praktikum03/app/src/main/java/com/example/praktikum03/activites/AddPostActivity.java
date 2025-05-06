package com.example.praktikum03.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praktikum03.R;
import com.example.praktikum03.models.Post;
import com.example.praktikum03.utils.DataSource;
import com.example.praktikum03.utils.ImageUtils;
import com.google.android.material.button.MaterialButton;

import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AddPostActivity";

    private ImageView imageViewBack, imageViewPostPreview;
    private MaterialButton buttonShare;
    private EditText editTextCaption;
    private Button buttonSelectImage;

    private Uri selectedImageUri;
    private boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewPostPreview = findViewById(R.id.iv_addPostPreview);
        buttonShare = findViewById(R.id.btn_share);
        editTextCaption = findViewById(R.id.et_caption);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);

        imageViewBack.setOnClickListener(v -> finish());

        buttonSelectImage.setOnClickListener(v -> {
            openGallery();
        });

        buttonShare.setOnClickListener(v -> {
            if (isImageSelected) {
                createNewPost();
            } else {
                Toast.makeText(AddPostActivity.this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                imageViewPostPreview.setImageURI(selectedImageUri);
                isImageSelected = true;
                Log.d("AddPostActivity", "Image selected: " + selectedImageUri.toString());
            } catch (Exception e) {
                Log.e(TAG, "Error setting image: " + e.getMessage());
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNewPost() {
        String caption = editTextCaption.getText().toString().trim();

        try {
            int newPostId = DataSource.getProfilePostList().size() + 200;

            BitmapDrawable drawable = (BitmapDrawable) imageViewPostPreview.getDrawable();
            Bitmap selectedBitmap = drawable.getBitmap();

            ImageUtils.addUploadedImage(newPostId, selectedBitmap);

            int defaultImageId = R.drawable.post_add1;

            Post newPost = new Post(
                    newPostId,
                    DataSource.getCurrentUser(),
                    defaultImageId,
                    caption,
                    0,
                    2,
                    1,
                    new Date()
            );

            DataSource.addPost(newPost);

            Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AddPostActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Error creating post: " + e.getMessage());
            Toast.makeText(this, "Error creating post", Toast.LENGTH_SHORT).show();
        }
    }
}
