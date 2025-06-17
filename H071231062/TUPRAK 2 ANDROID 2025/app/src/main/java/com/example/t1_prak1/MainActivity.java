package com.example.t1_prak1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private Button buttonEdit;
    private TextView namaTextView, deskripsiTextView, bioTextView, locationTextView;
    private ImageView profileImageView;
    private static final String PROFILE_DATA_FILE = "profile_data.json";

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    Intent data = result.getData();
                    String newName = data.getStringExtra("name");
                    String newUsername = data.getStringExtra("username");
                    String newBio = data.getStringExtra("bio");
                    String newLocation = data.getStringExtra("location");
                    String profileImageUriString = data.getStringExtra("profileImageUri");

                    if (newName != null && !newName.isEmpty()) {
                        namaTextView.setText(newName);
                        saveToJson("name", newName);
                    }

                    if (newUsername != null && !newUsername.isEmpty()) {
                        String descText = newUsername + " • Man";
                        deskripsiTextView.setText(descText);
                        saveToJson("username", newUsername);
                    }

                    if (newBio != null && !newBio.isEmpty()) {
                        bioTextView.setText(newBio);
                        saveToJson("bio", newBio);
                    }

                    if (newLocation != null && !newLocation.isEmpty()) {
                        locationTextView.setText(newLocation);
                        saveToJson("location", newLocation);
                    }

                    if (profileImageUriString != null) {
                        Uri profileImageUri = Uri.parse(profileImageUriString);
                        profileImageView.setImageURI(profileImageUri);
                        saveImageLocally(profileImageUri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        buttonEdit = findViewById(R.id.button_edit);
        namaTextView = findViewById(R.id.nama);
        deskripsiTextView = findViewById(R.id.deskripsi);
        bioTextView = findViewById(R.id.bio_text);
        locationTextView = findViewById(R.id.location_text);
        profileImageView = findViewById(R.id.profileimage);

        buttonEdit.setText("Edit");
        buttonEdit.setBackgroundColor(getResources().getColor(android.R.color.white));
        buttonEdit.setTextColor(getResources().getColor(android.R.color.black));


        loadSavedData();

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(MainActivity.this, View2Edit.class);

                editProfileIntent.putExtra("name", namaTextView.getText().toString());

                String description = deskripsiTextView.getText().toString();
                String[] parts = description.split(" • ");
                String username = parts[0];

                editProfileIntent.putExtra("username", username);
                editProfileIntent.putExtra("bio", bioTextView.getText().toString());
                editProfileIntent.putExtra("location", locationTextView.getText().toString());

                String savedImagePath = getFromJson("profile_image_path");
                if (savedImagePath != null && !savedImagePath.isEmpty()) {
                    editProfileIntent.putExtra("savedImagePath", savedImagePath);
                }

                // Launch the edit profile activity
                editProfileLauncher.launch(editProfileIntent);
            }
        });
    }

    private JSONObject readJsonFile() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (fileExists(PROFILE_DATA_FILE)) {
                FileInputStream fis = openFileInput(PROFILE_DATA_FILE);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                isr.close();
                fis.close();

                jsonObject = new JSONObject(sb.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void writeJsonFile(JSONObject jsonObject) {
        try {
            FileOutputStream fos = openFileOutput(PROFILE_DATA_FILE, MODE_PRIVATE);
            fos.write(jsonObject.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fileExists(String filename) {
        File file = new File(getFilesDir(), filename);
        return file.exists();
    }

    private void saveToJson(String key, String value) {
        try {
            JSONObject jsonObject = readJsonFile();
            jsonObject.put(key, value);
            writeJsonFile(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getFromJson(String key) {
        String value = "";
        try {
            JSONObject jsonObject = readJsonFile();
            if (jsonObject.has(key)) {
                value = jsonObject.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void loadSavedData() {
        String savedName = getFromJson("name");
        String savedUsername = getFromJson("username");
        String savedBio = getFromJson("bio");
        String savedLocation = getFromJson("location");

        if (savedName != null && !savedName.isEmpty()) {
            namaTextView.setText(savedName);
        }

        if (savedUsername != null && !savedUsername.isEmpty()) {
            String descText = savedUsername + " • Man";
            deskripsiTextView.setText(descText);
        }

        if (savedBio != null && !savedBio.isEmpty()) {
            bioTextView.setText(savedBio);
        }

        if (savedLocation != null && !savedLocation.isEmpty()) {
            locationTextView.setText(savedLocation);
        }

        // Load saved profile image
        String savedImagePath = getFromJson("profile_image_path");
        if (savedImagePath != null && !savedImagePath.isEmpty()) {
            File imageFile = new File(savedImagePath);
            if (imageFile.exists()) {
                profileImageView.setImageURI(Uri.fromFile(imageFile));
            }
        }
    }

    private void saveImageLocally(Uri imageUri) {
        try {
            // Create directory for app images if it doesn't exist
            File directory = new File(getFilesDir(), "profile_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a file to save the image
            File imageFile = new File(directory, "profile_image.jpg");

            // Copy the image from URI to the file
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            // Save the file path to JSON
            saveToJson("profile_image_path", imageFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}