package com.example.onstagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AddFeed extends AppCompatActivity {

    private EditText captionInput;
    private ImageView previewImage;
    private Button submitBtn;

    private User currentUser;
    private ArrayList<Feed> userFeeds;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri = null; // Menggunakan URI gambar langsung

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_feed);

        captionInput = findViewById(R.id.caption_input);
        previewImage = findViewById(R.id.preview_image);
        submitBtn = findViewById(R.id.submit_btn);

        currentUser = getIntent().getParcelableExtra("user");
        userFeeds = getIntent().getParcelableArrayListExtra("userFeeds");

        previewImage.setOnClickListener(v -> openFileChooser());

        submitBtn.setOnClickListener(v -> {
            String caption = captionInput.getText().toString().trim();

            if (imageUri == null || caption.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom", Toast.LENGTH_SHORT).show();
                return;
            }

            String imagePath = imageUri.toString();

            Feed newFeed = new Feed(imagePath, caption, currentUser.getUsername());
            if (userFeeds != null) {
                userFeeds.add(0, newFeed);
                currentUser.setPostCount(currentUser.getPostCount() + 1);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("newFeed", newFeed);
            resultIntent.putExtra("user", currentUser);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(previewImage);
        }
    }
}
