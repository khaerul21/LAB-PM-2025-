package com.example.ig_profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    // Definisikan konstanta untuk Intent Extras (pastikan konsisten dengan EditProfileActivity)
    public static final String EXTRA_USERNAME = "com.example.ig_profile.EXTRA_USERNAME";
    public static final String EXTRA_NAME = "com.example.ig_profile.EXTRA_NAME";
    public static final String EXTRA_BIO = "com.example.ig_profile.EXTRA_BIO";
    public static final String EXTRA_PROFILE_IMAGE_URI = "com.example.ig_profile.EXTRA_PROFILE_IMAGE_URI";

    private TextView tvUsername;
    private TextView tvName;
    private TextView tvBio;
    private ImageView ivProfileImage;

    private String currentProfileImageUriString = null;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scrollable);

        // Pastikan ID root layout cocok (misal: R.id.main_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi Views
        tvUsername = findViewById(R.id.username);
        tvName = findViewById(R.id.name);
        tvBio = findViewById(R.id.bio);
        ivProfileImage = findViewById(R.id.profile_image);

        Button editBtn = findViewById(R.id.edit_profile_btn);

        int initialImageResId = R.drawable.ig_profile;
        ivProfileImage.setImageResource(initialImageResId);
        currentProfileImageUriString = getUriStringFromDrawableResId(this, initialImageResId);

        // Inisialisasi ActivityResultLauncher
        editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String updatedUsername = data.getStringExtra(EXTRA_USERNAME);
                    String updatedName = data.getStringExtra(EXTRA_NAME);
                    String updatedBio = data.getStringExtra(EXTRA_BIO);
                    String newImageUriString = data.getStringExtra(EXTRA_PROFILE_IMAGE_URI);

                    if (updatedUsername != null) {
                        tvUsername.setText(updatedUsername);
                    }
                    if (updatedName != null) {
                        tvName.setText(updatedName);
                    }
                    if (updatedBio != null) {
                        tvBio.setText(updatedBio);
                    }

                    if (newImageUriString != null && !newImageUriString.isEmpty()) {
                        Uri imageUri = Uri.parse(newImageUriString);
                        ivProfileImage.setImageURI(imageUri);
                        currentProfileImageUriString = newImageUriString;
                    }

                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                }
            });

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            intent.putExtra(EXTRA_USERNAME, tvUsername.getText().toString());
            intent.putExtra(EXTRA_NAME, tvName.getText().toString());
            intent.putExtra(EXTRA_BIO, tvBio.getText().toString());
            intent.putExtra(EXTRA_PROFILE_IMAGE_URI, currentProfileImageUriString);
            editProfileLauncher.launch(intent);
        });
    }

    /**
     * Helper method untuk mendapatkan String URI dari resource ID drawable.
     * @param context Context aplikasi
     * @param drawableResId ID resource drawable
     * @return String URI (misal: android.resource://com.example.yourapp/drawable/icon_name) atau null jika ID 0
     */
    public static String getUriStringFromDrawableResId(Context context, int drawableResId) {
        if (context == null || drawableResId == 0) return null;
        try {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + context.getResources().getResourcePackageName(drawableResId) +
                    '/' + context.getResources().getResourceTypeName(drawableResId) +
                    '/' + context.getResources().getResourceEntryName(drawableResId)).toString();
        } catch (Exception e) {
            // Jika resource tidak ditemukan
            e.printStackTrace();
            return null;
        }
    }
}