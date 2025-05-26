package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity"; //1
    private static final int PICK_IMAGE_REQUEST = 101; //1
    private static final String PREFS_NAME = "ProfilePrefs"; //1

    private EditText editName, editUsername, editBio; //1
    private ImageView profilePhoto; //2
    private Button btnSave, btnCancel; //2
    private String profilePhotoUriString = null; //2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile); //3

        editName     = findViewById(R.id.edit_name); //3
        editUsername = findViewById(R.id.edit_username); //3
        editBio      = findViewById(R.id.edit_bio); //3
        profilePhoto = findViewById(R.id.profile_photo);//3
        btnSave      = findViewById(R.id.btn_save); //3
        btnCancel    = findViewById(R.id.btn_cancel); //3

        profilePhoto.setImageResource(R.drawable.p2); //3

        // Prefill dari prefs
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); //4
        editName.setText(prefs.getString("name", "")); //4
        editUsername.setText(prefs.getString("username", "")); //4
        editBio.setText(prefs.getString("bio", "")); //4

        // Override dari Intent extras
        Intent intent = getIntent(); //5
        if (intent != null) {
            String n = intent.getStringExtra("name");
            String u = intent.getStringExtra("username");
            String b = intent.getStringExtra("bio");
            String p = intent.getStringExtra("photoUri");
            if (n != null) editName.setText(n);
            if (u != null) editUsername.setText(u);
            if (b != null) editBio.setText(b);
            if (p != null) {
                profilePhotoUriString = p;
                try {
                    profilePhoto.setImageURI(Uri.parse(p));
                } catch (Exception e) {
                    Log.e(TAG, "Error loading photoUri", e);
                    Toast.makeText(this, "Tidak dapat memuat foto profil", Toast.LENGTH_SHORT).show();
                }
            }
        }
            //6
        profilePhoto.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
            gallery.setType("image/*");
            startActivityForResult(Intent.createChooser(gallery, "Pilih Foto Profil"), PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> { //7
            String name     = editName.getText().toString().trim();
            String username = editUsername.getText().toString().trim();
            String bio      = editBio.getText().toString().trim();

            if (name.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "Nama dan username harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan ke prefs
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("name", name);
            editor.putString("username", username);
            editor.putString("bio", bio);
            editor.apply();

            // Kirim balik hasil
            Intent result = new Intent();
            result.putExtra("name", name);
            result.putExtra("username", username);
            result.putExtra("bio", bio);
            if (profilePhotoUriString != null) {
                result.putExtra("photoUri", profilePhotoUriString);
            }
            setResult(RESULT_OK, result);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish()); //8
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //9
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            profilePhotoUriString = uri.toString();
            try {
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                profilePhoto.setImageURI(uri);
                Log.d(TAG, "New profile photo URI: " + profilePhotoUriString);
            } catch (Exception e) {
                Log.e(TAG, "Error handling selected image", e);
                Toast.makeText(this, "Gagal memuat foto yang dipilih", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
