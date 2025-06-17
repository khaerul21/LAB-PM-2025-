package com.example.tp_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> editProfileLauncher;
    private UserProfile userProfile;

    private TextView nameTextView, usernameTextView, bioTextView, pronounsTextView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi data awal profil
        userProfile = new UserProfile(
                "sisfouh23",
                "Sistem Informasi UH'23",
                "they",
                "Universitas Hasanuddin\nFakultas MIPA",
                ""
        );

        // Inisialisasi komponen UI
        nameTextView = findViewById(R.id.infoDetails);
        usernameTextView = findViewById(R.id.labelUser);
        bioTextView = findViewById(R.id.bio);
        pronounsTextView = findViewById(R.id.pronouns);
        profileImageView = findViewById(R.id.profile);

        // Tampilkan data awal di UI
        displayUserProfile(userProfile);

        // Klik link ke GitHub
        TextView linkTextView = findViewById(R.id.link);
        linkTextView.setOnClickListener(v -> {
            String url = "https://github.com/havyasi";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Menangani hasil dari EditProfileActivity
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        UserProfile updatedProfile = result.getData().getParcelableExtra("updatedProfile");
                        if (updatedProfile != null) {
                            userProfile = updatedProfile;
                            displayUserProfile(userProfile);
                        }
                    }
                }
        );

        // Tombol edit profil
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            intent.putExtra("userProfile", userProfile);
            editProfileLauncher.launch(intent);
        });
    }

    // Menampilkan data profil ke UI
    private void displayUserProfile(UserProfile profile) {
        if (profile == null) return;

        nameTextView.setText(profile.getName() != null ? profile.getName() : "-");
        usernameTextView.setText(profile.getUsername() != null ? profile.getUsername() : "-");
        bioTextView.setText(profile.getBio() != null ? profile.getBio() : "-");
        pronounsTextView.setText(profile.getPronouns() != null ? profile.getPronouns() : "-");

        // Tampilkan gambar profil atau default
        if (profile.getProfilePhotoUri() != null && !profile.getProfilePhotoUri().isEmpty()) {
            profileImageView.setImageURI(Uri.parse(profile.getProfilePhotoUri()));
        } else {
            profileImageView.setImageResource(R.drawable.logsis); // Gambar default
        }
    }
}
