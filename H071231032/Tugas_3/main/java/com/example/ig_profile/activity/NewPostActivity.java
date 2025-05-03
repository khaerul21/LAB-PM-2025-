package com.example.ig_profile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ig_profile.DataHolder;
import com.example.ig_profile.R;
import com.example.ig_profile.models.FeedItem;
import com.example.ig_profile.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NewPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imagePreview;
    private EditText captionInput;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        imagePreview = findViewById(R.id.image_preview);
        captionInput = findViewById(R.id.caption_input);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button saveButton = findViewById(R.id.save_button);

        imagePreview.setOnClickListener(v -> openGallery());
        setupBottomBar();

        cancelButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                String caption = captionInput.getText().toString();

                // Simpan ke feed current user
                User currentUser = DataHolder.getInstance().getCurrentUser();
                Uri safeUri = copyUriToInternalStorage(selectedImageUri);
                FeedItem item = new FeedItem(safeUri, caption);
                item.setOwnerFeeds(currentUser.getUsername());
                currentUser.addFeedItem(item);
                DataHolder.getInstance().setCurrentUser(currentUser);

                Toast.makeText(this, "Post saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Uri copyUriToInternalStorage(Uri srcUri) {
        try {
            String filename = "feed_" + System.currentTimeMillis() + ".jpg";
            File destFile = new File(getFilesDir(), filename);

            try (InputStream in = getContentResolver().openInputStream(srcUri);
                 OutputStream out = new FileOutputStream(destFile)) {

                byte[] buffer = new byte[1024];
                int len;
                while (true) {
                    assert in != null;
                    if (!((len = in.read(buffer)) > 0)) break;
                    out.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);

            }

            return Uri.fromFile(destFile);

        } catch (IOException e) {
            e.fillInStackTrace();
            return null;
        }
    }

    private void setupBottomBar() {
        ImageView homeActivity = findViewById(R.id.home_activity);
        ImageView profileActivity = findViewById(R.id.profile_activity);

        homeActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        profileActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);
        } else {
            finish();
        }
    }
}