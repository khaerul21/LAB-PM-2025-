package com.example.instagramclone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> pickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupImagePicker();
        setupToolbar();
        setupClickListeners();
    }

    private void setupImagePicker() {
        pickImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        binding.imagePreview.setImageURI(uri);
                        binding.imagePreview.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("New Post");
        }
    }

    private void setupClickListeners() {
        binding.selectImageButton.setOnClickListener(v ->
                pickImage.launch("image/*")
        );

        binding.postButton.setOnClickListener(v -> {
            String caption = binding.captionInput.getText().toString();
            if (selectedImageUri != null && !caption.isEmpty()) {
                // Here you would typically upload the image and create the post
                Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Please select an image and add a caption", Toast.LENGTH_SHORT).show();
            }
        });
    }
}