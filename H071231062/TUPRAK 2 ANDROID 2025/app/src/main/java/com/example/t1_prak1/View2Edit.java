package com.example.t1_prak1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class View2Edit extends AppCompatActivity {

    private Button buttonBack, buttonSave, buttonChangePhoto;
    private EditText editName, editUsername, editBio, editLocation;
    private ImageView profileImage;
    private Uri selectedImageUri;
    private String savedImagePath;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        profileImage.setImageURI(selectedImageUri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view2_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        buttonBack = findViewById(R.id.button_back);
        buttonSave = findViewById(R.id.button_save);
        buttonChangePhoto = findViewById(R.id.button_change_photo);
        editName = findViewById(R.id.edit_name);
        editUsername = findViewById(R.id.edit_username);
        editBio = findViewById(R.id.edit_bio);
        editLocation = findViewById(R.id.edit_location);
        profileImage = findViewById(R.id.edit_profile_image);

        String currentName = getIntent().getStringExtra("name");
        String currentUsername = getIntent().getStringExtra("username");
        String currentBio = getIntent().getStringExtra("bio");
        String currentLocation = getIntent().getStringExtra("location");
        savedImagePath = getIntent().getStringExtra("savedImagePath");


        if(currentName != null) editName.setText(currentName);
        if(currentUsername != null) editUsername.setText(currentUsername);
        if(currentBio != null) editBio.setText(currentBio);
        if(currentLocation != null) editLocation.setText(currentLocation);


        if(savedImagePath != null && !savedImagePath.isEmpty()) {
            File imageFile = new File(savedImagePath);
            if(imageFile.exists()) {
                selectedImageUri = Uri.fromFile(imageFile);
                profileImage.setImageURI(selectedImageUri);
            }
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void saveChanges() {

        Intent resultIntent = new Intent();


        resultIntent.putExtra("name", editName.getText().toString());
        resultIntent.putExtra("username", editUsername.getText().toString());
        resultIntent.putExtra("bio", editBio.getText().toString());
        resultIntent.putExtra("location", editLocation.getText().toString());

        if (selectedImageUri != null) {
            resultIntent.putExtra("profileImageUri", selectedImageUri.toString());
        }


        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}