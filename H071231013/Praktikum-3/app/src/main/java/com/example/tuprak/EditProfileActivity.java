package com.example.tuprak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    
    private EditText editName;
    private EditText editUsername;
    private EditText editKataGanti;
    private EditText editBio;
    private EditText editLinks;
    private TextView editGender;
    private ImageView profileImage;
    private ImageView avatarImage;
    private ImageView backButton;
    private Switch threadsToggle;
    private TextView editPhotoText;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        profileImage.setImageURI(selectedImageUri);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> nameEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newName = result.getData().getStringExtra("new_name");
                    if (newName != null && !newName.isEmpty()) {
                        editName.setText(newName);
                    }
                }
            });
            
    private final ActivityResultLauncher<Intent> usernameEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newUsername = result.getData().getStringExtra("new_username");
                    if (newUsername != null && !newUsername.isEmpty()) {
                        editUsername.setText(newUsername);
                    }
                }
            });
            
    private final ActivityResultLauncher<Intent> kataGantiEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newKataGanti = result.getData().getStringExtra("new_kataganti");
                    if (newKataGanti != null) {
                        editKataGanti.setText(newKataGanti);
                    }
                }
            });
            
    private final ActivityResultLauncher<Intent> bioEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newBio = result.getData().getStringExtra("new_bio");
                    if (newBio != null) {
                        editBio.setText(newBio);
                    }
                }
            });
            
    private final ActivityResultLauncher<Intent> genderEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newGender = result.getData().getStringExtra("new_gender");
                    
                    if (newGender != null && !newGender.isEmpty()) {
                        editGender.setText(newGender);
                        Toast.makeText(EditProfileActivity.this, 
                            "Gender updated to: " + newGender, Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        editKataGanti = findViewById(R.id.kataGantiEditField);
        editBio = findViewById(R.id.editBio);
        editLinks = findViewById(R.id.editLinks);
        profileImage = findViewById(R.id.profileImage);
        avatarImage = findViewById(R.id.avatarImage);
        backButton = findViewById(R.id.backButton);
        threadsToggle = findViewById(R.id.threadsToggle);
        editPhotoText = findViewById(R.id.editPhotoText);
        
        RelativeLayout genderLayout = findViewById(R.id.genderLayout);
        editGender = findViewById(R.id.editGender);

        editName.setFocusable(false);
        editName.setClickable(true);
        
        editUsername.setFocusable(false);
        editUsername.setClickable(true);
        
        editKataGanti.setFocusable(false);
        editKataGanti.setClickable(true);
        
        editBio.setFocusable(false);
        editBio.setClickable(true);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            String kataGanti = intent.getStringExtra("kataganti");
            String bio = intent.getStringExtra("bio");
            String links = intent.getStringExtra("links");
            String gender = intent.getStringExtra("gender");

            if (intent.hasExtra("profileImageUri")) {
                String imageUriString = intent.getStringExtra("profileImageUri");
                if (imageUriString != null && !imageUriString.isEmpty()) {
                    selectedImageUri = Uri.parse(imageUriString);
                    profileImage.setImageURI(selectedImageUri);
                }
            }

            if (name != null)
                editName.setText(name);
            if (username != null)
                editUsername.setText(username);
            if (kataGanti != null)
                editKataGanti.setText(kataGanti);
            if (bio != null)
                editBio.setText(bio);
            if (links != null)
                editLinks.setText(links);
            if (gender != null)
                editGender.setText(gender);
        }

        backButton.setOnClickListener(v -> {
            saveChanges();
            finish();
        });

        editName.setOnClickListener(v -> {
            Intent nameEditIntent = new Intent(EditProfileActivity.this, NameEditActivity.class);
            nameEditIntent.putExtra("current_name", editName.getText().toString());
            nameEditLauncher.launch(nameEditIntent);
        });
        
        editUsername.setOnClickListener(v -> {
            Intent usernameEditIntent = new Intent(EditProfileActivity.this, UsernameEditActivity.class);
            usernameEditIntent.putExtra("current_username", editUsername.getText().toString());
            usernameEditLauncher.launch(usernameEditIntent);
        });
        
        editKataGanti.setOnClickListener(v -> {
            Intent kataGantiEditIntent = new Intent(EditProfileActivity.this, KataGantiEditActivity.class);
            kataGantiEditIntent.putExtra("current_kataganti", editKataGanti.getText().toString());
            kataGantiEditLauncher.launch(kataGantiEditIntent);
        });
        
        editBio.setOnClickListener(v -> {
            Intent bioEditIntent = new Intent(EditProfileActivity.this, BioEditActivity.class);
            bioEditIntent.putExtra("current_bio", editBio.getText().toString());
            bioEditLauncher.launch(bioEditIntent);
        });
        
        genderLayout.setOnClickListener(v -> {
            String currentGender = editGender.getText().toString();
            Intent genderEditIntent = new Intent(EditProfileActivity.this, GenderEditActivity.class);
            genderEditIntent.putExtra("current_gender", currentGender);
            genderEditLauncher.launch(genderEditIntent);
        });

        editPhotoText.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            pickImageLauncher.launch(photoPickerIntent);
        });

        profileImage.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            pickImageLauncher.launch(photoPickerIntent);
        });

        avatarImage.setOnClickListener(v -> {
            Toast.makeText(EditProfileActivity.this, "Avatar selection not implemented", Toast.LENGTH_SHORT).show();
        });

        TextView switchToProfessional = findViewById(R.id.switchToProfessional);
        TextView privacySettings = findViewById(R.id.privacySettings);
    }


    private void saveChanges() {
        if (editUsername.getText().toString().trim().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = editGender.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", editName.getText().toString());
        resultIntent.putExtra("username", editUsername.getText().toString());
        resultIntent.putExtra("kataganti", editKataGanti.getText().toString());
        resultIntent.putExtra("bio", editBio.getText().toString());
        resultIntent.putExtra("links", editLinks.getText().toString());
        resultIntent.putExtra("gender", gender); 
        resultIntent.putExtra("threadsEnabled", threadsToggle.isChecked());

        if (selectedImageUri != null) {
            resultIntent.putExtra("profileImageUri", selectedImageUri.toString());
        }

        setResult(RESULT_OK, resultIntent);
    }
}