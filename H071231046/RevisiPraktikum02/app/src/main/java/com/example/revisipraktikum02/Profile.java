package com.example.revisipraktikum02;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Profile extends AppCompatActivity {

    private ImageView imgProfilePreview;
    private TextView tvPreviewName, tvPreviewUsername, tvPreviewEmail;
    private Button btnBack;
    private String selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi komponen
        imgProfilePreview = findViewById(R.id.imgProfilePreview);
        tvPreviewName = findViewById(R.id.tvPreviewName);
        tvPreviewUsername = findViewById(R.id.tvPreviewUsername);
        tvPreviewEmail = findViewById(R.id.tvPreviewEmail);
        btnBack = findViewById(R.id.btnBack);

        // Toolbar (opsional)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ambil data dari intent
        UserProfile user = getIntent().getParcelableExtra("UPDATED_USER");

        if (user != null) {
            tvPreviewName.setText(user.getName());
            tvPreviewUsername.setText(user.getUserId());
            tvPreviewEmail.setText(user.getEmail());
            selectedImageUri = user.getImageUri();

            if (selectedImageUri != null && !selectedImageUri.isEmpty()) {
                imgProfilePreview.setImageURI(Uri.parse(selectedImageUri));
            }
        }


        // Tombol kembali ke MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, MainActivity.class);
            // Kirim data kembali ke MainActivity
            intent.putExtra("UPDATED_USER", new UserProfile(
                    tvPreviewName.getText().toString(),
                    tvPreviewUsername.getText().toString(),
                    "", // password
                    tvPreviewEmail.getText().toString(),
                    selectedImageUri != null ? selectedImageUri : ""
            ));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        });
    }
}
