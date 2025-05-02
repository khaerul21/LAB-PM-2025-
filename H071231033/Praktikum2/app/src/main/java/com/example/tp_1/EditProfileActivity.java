package com.example.tp_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EDIT_NAME = 100;
    private static final int REQUEST_EDIT_USERNAME = 101;
    private static final int REQUEST_EDIT_PRONOUNS = 102;
    private static final int REQUEST_EDIT_BIO = 103;

    private ImageView profileImageView;
    private TextView changeProfilePictureText;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi UI
        ImageView backButton = findViewById(R.id.back);
        profileImageView = findViewById(R.id.profile);
        changeProfilePictureText = findViewById(R.id.change_pp);

        TextView nameTextView = findViewById(R.id.name);
        TextView usernameTextView = findViewById(R.id.username);
        TextView pronounsTextView = findViewById(R.id.pronouns);
        TextView bioTextView = findViewById(R.id.bio);

        // Ambil data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            userProfile = intent.getParcelableExtra("userProfile");

            if (userProfile != null) {
                usernameTextView.setText(userProfile.getUsername());
                nameTextView.setText(userProfile.getName());
                pronounsTextView.setText(userProfile.getPronouns());
                bioTextView.setText(userProfile.getBio());

                if (!userProfile.getProfilePhotoUri().isEmpty()) {
                    Uri profileUri = Uri.parse(userProfile.getProfilePhotoUri());
                    profileImageView.setImageURI(profileUri);
                }
            }
        }

        // Tombol balik ke halaman sebelumnya
        backButton.setOnClickListener(v -> {
            updateUserProfile();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedProfile", userProfile);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Buka galeri untuk ganti foto
        profileImageView.setOnClickListener(v -> openGallery());
        changeProfilePictureText.setOnClickListener(v -> openGallery());

        // Intent ke masing-masing halaman edit
        nameTextView.setOnClickListener(v -> {
            Intent i = new Intent(this, EditNameActivity.class);
            i.putExtra("currentName", userProfile.getName());
            startActivityForResult(i, REQUEST_EDIT_NAME);
        });

        usernameTextView.setOnClickListener(v -> {
            Intent i = new Intent(this, EditUsernameActivity.class);
            i.putExtra("currentUsername", userProfile.getUsername());
            startActivityForResult(i, REQUEST_EDIT_USERNAME);
        });

        pronounsTextView.setOnClickListener(v -> {
            Intent i = new Intent(this, EditPronounsActivity.class);
            i.putExtra("currentPronouns", userProfile.getPronouns());
            startActivityForResult(i, REQUEST_EDIT_PRONOUNS);
        });

        bioTextView.setOnClickListener(v -> {
            Intent i = new Intent(this, EditBioActivity.class);
            i.putExtra("currentBio", userProfile.getBio());
            startActivityForResult(i, REQUEST_EDIT_BIO);
        });
    }

    // Buka galeri
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // ini buat urus edit-editnya
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    Uri imageUri = data.getData();
                    if (imageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            profileImageView.setImageBitmap(bitmap);
                            userProfile.setProfilePhotoUri(imageUri.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case REQUEST_EDIT_NAME:
                    String updatedName = data.getStringExtra("updatedName");
                    if (updatedName != null) {
                        userProfile.setName(updatedName);
                        ((TextView) findViewById(R.id.name)).setText(updatedName);
                    }
                    break;

                case REQUEST_EDIT_USERNAME:
                    String updatedUsername = data.getStringExtra("updatedUsername");
                    if (updatedUsername != null) {
                        userProfile.setUsername(updatedUsername);
                        ((TextView) findViewById(R.id.username)).setText(updatedUsername);
                    }
                    break;

                case REQUEST_EDIT_PRONOUNS:
                    String updatedPronouns = data.getStringExtra("updatedPronouns");
                    if (updatedPronouns != null) {
                        userProfile.setPronouns(updatedPronouns);
                        ((TextView) findViewById(R.id.pronouns)).setText(updatedPronouns);
                    }
                    break;

                case REQUEST_EDIT_BIO:
                    String updatedBio = data.getStringExtra("updatedBio");
                    if (updatedBio != null) {
                        userProfile.setBio(updatedBio);
                        ((TextView) findViewById(R.id.bio)).setText(updatedBio);
                    }
                    break;
            }
        }
    }

    // buat update data di UserProfile
    private void updateUserProfile() {
        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        String name = ((TextView) findViewById(R.id.name)).getText().toString();
        String pronouns = ((TextView) findViewById(R.id.pronouns)).getText().toString();
        String bio = ((TextView) findViewById(R.id.bio)).getText().toString();

        if (username.isEmpty() || name.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        userProfile.setUsername(username);
        userProfile.setName(name);
        userProfile.setPronouns(pronouns);
        userProfile.setBio(bio);
    }
}

