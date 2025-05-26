package com.example.tuprak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuprak.models.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CaptionActivity extends AppCompatActivity {

    private static final int MAX_CAPTION_LENGTH = 2200;

    private ImageView previewImage;
    private EditText captionInput;
    private TextView charCounter;
    private TextView shareButton;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        previewImage = findViewById(R.id.previewImage);
        captionInput = findViewById(R.id.captionInput);
        charCounter = findViewById(R.id.charCounter);
        shareButton = findViewById(R.id.shareButton);
        
        ImageView profileImageView = findViewById(R.id.profileImageView);
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        ProfilePreferencesManager preferencesManager = new ProfilePreferencesManager(this);
        String username = preferencesManager.getUsername("user");
        Uri profileImageUri = preferencesManager.getProfileImageUri();
        
        usernameTextView.setText(username);
        
        if (profileImageUri != null) {
            try {
                profileImageView.setImageURI(profileImageUri);
            } catch (Exception e) {
                profileImageView.setImageResource(R.drawable.lab_logo);
            }
        } else {
            profileImageView.setImageResource(R.drawable.lab_logo);
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            previewImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            finish();
        }

        captionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateCharCounter(s.length());
            }
        });

        updateCharCounter(0);

        shareButton.setOnClickListener(v -> createPost());
    }

    private void updateCharCounter(int length) {
        charCounter.setText(length + "/" + MAX_CAPTION_LENGTH);

        if (length > MAX_CAPTION_LENGTH) {
            charCounter.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            shareButton.setEnabled(false);
            shareButton.setAlpha(0.5f);
        } else {
            charCounter.setTextColor(getResources().getColor(android.R.color.darker_gray));
            shareButton.setEnabled(true);
            shareButton.setAlpha(1.0f);
        }
    }

    private void createPost() {
        String caption = captionInput.getText().toString().trim();
        if (caption.length() > MAX_CAPTION_LENGTH) {
            Toast.makeText(this, "Caption is too long", Toast.LENGTH_SHORT).show();
            return;
        }

        ProfilePreferencesManager preferencesManager = new ProfilePreferencesManager(this);
        String username = preferencesManager.getUsername("user");
        Uri profileImageUri = preferencesManager.getProfileImageUri();
        PostManager postManager = new PostManager(this);
        String postId = postManager.saveNewPost(imageUri, username, profileImageUri, caption);

        if (postId != null) {
            Intent updateIntent = new Intent("com.example.tuprak.NEW_POST_CREATED");
            sendBroadcast(updateIntent);

            Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show();
        }
    }
}