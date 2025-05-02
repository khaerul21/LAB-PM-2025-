package com.example.ig_profile.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ig_profile.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditProfileActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = ProfileActivity.EXTRA_USERNAME;
    public static final String EXTRA_NAME = ProfileActivity.EXTRA_NAME;
    public static final String EXTRA_BIO = ProfileActivity.EXTRA_BIO;
    public static final String EXTRA_PROFILE_IMAGE_URI = ProfileActivity.EXTRA_PROFILE_IMAGE_URI;

    private EditText etName, etUsername, etBio;
    private ImageView ivProfileImage;
    private TextView tvChangeProfilePic;
    private ImageButton btnBack, btnSave;

    private String initialName = "";
    private String initialUsername = "";
    private String initialBio = "";
    private String initialProfileImageUriString = "";
    private boolean isProfilePicChanged = false;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit_profile_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.edit_text_name);
        etUsername = findViewById(R.id.edit_text_username);
        etBio = findViewById(R.id.edit_text_bio);
        ivProfileImage = findViewById(R.id.profile_image_edit);
        tvChangeProfilePic = findViewById(R.id.change_profile_pict);
        btnBack = findViewById(R.id.back_to_profile);
        btnSave = findViewById(R.id.save_profile_button);

        setupImagePickerLauncher();
        loadInitialData();
        setupButtonClickListeners();
        setupTextWatchers();
        updateSaveButtonState();

        setupOnBackPressedHandling();

    }

    private void loadInitialData() {
        Intent intent = getIntent();
        if (intent != null) {
            initialName = intent.getStringExtra(EXTRA_NAME);
            initialUsername = intent.getStringExtra(EXTRA_USERNAME);
            initialBio = intent.getStringExtra(EXTRA_BIO);
            initialProfileImageUriString = intent.getStringExtra(EXTRA_PROFILE_IMAGE_URI);

            etName.setText(initialName != null ? initialName : "");
            etUsername.setText(initialUsername != null ? initialUsername : "");
            etBio.setText(initialBio != null ? initialBio : "");

            if (initialProfileImageUriString != null) {
                try {
                    Uri imageUri = Uri.parse(initialProfileImageUriString);
                    ivProfileImage.setImageURI(imageUri);
                } catch (Exception e) {
                    Log.e("EditProfileActivity", "Error parsing initial image URI: " + initialProfileImageUriString, e);
                    ivProfileImage.setImageResource(R.drawable.ig_profile);
                }
            }
        }
    }
    private void setupButtonClickListeners() {
        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            if (name.contains("\n")) {
                etName.setError("The name must not contain newlines");
                return;
            }

            String username = etUsername.getText().toString();
            if (!username.matches("^[a-zA-Z0-9._]*$")) {
                etUsername.setError("The username can only be letters, numbers, dots, and underscores");
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_NAME, etName.getText().toString());
            resultIntent.putExtra(EXTRA_USERNAME, etUsername.getText().toString());
            resultIntent.putExtra(EXTRA_BIO, etBio.getText().toString());
            resultIntent.putExtra(EXTRA_PROFILE_IMAGE_URI,
                    initialProfileImageUriString != null ? initialProfileImageUriString : "");

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        tvChangeProfilePic.setOnClickListener(v -> {
            Log.d("EditProfileActivity", "Change profile picture clicked");
            imagePickerLauncher.launch("image/*");
        });
    }
    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateSaveButtonState(); }
        };
        etName.addTextChangedListener(textWatcher);
        etUsername.addTextChangedListener(textWatcher);
        etBio.addTextChangedListener(textWatcher);
    }
    private void updateSaveButtonState() {
        boolean nameChanged = !etName.getText().toString().equals(initialName != null ? initialName : "");
        boolean usernameChanged = !etUsername.getText().toString().equals(initialUsername != null ? initialUsername : "");
        boolean bioChanged = !etBio.getText().toString().equals(initialBio != null ? initialBio : "");
        boolean imageChanged = isProfilePicChanged;
        boolean hasChanges = nameChanged || usernameChanged || bioChanged || imageChanged;
        btnSave.setEnabled(hasChanges);
        Log.d("EditProfileActivity", "UpdateSaveButton: HasChanges=" + hasChanges);
    }

    private Uri copyUriToInternalStorage(Uri srcUri) {
        try {
            String filename = "profile_" + System.currentTimeMillis() + ".jpg";
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

    private void setupImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    Log.d("EditProfileActivity", "Image selected: " + uri);

                    Uri localUri = copyUriToInternalStorage(uri);
                    if (localUri != null) {
                        isProfilePicChanged = true;
                        initialProfileImageUriString = localUri.toString();
                        ivProfileImage.setImageURI(localUri);
                        updateSaveButtonState();
                    } else {
                        Toast.makeText(this, "Failed to copy image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("EditProfileActivity", "Image selection cancelled");
                }
            });
    }

    private void setupOnBackPressedHandling() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (btnSave.isEnabled()) {
                    // Ada perubahan yang belum disimpan, tampilkan dialog konfirmasi
                    new AlertDialog.Builder(EditProfileActivity.this)
                            .setTitle("Discard Changes?")
                            .setMessage("You have unsaved changes. Are you sure you want to discard them?")
                            .setPositiveButton("Discard", (dialog, which) -> {
                                // Pengguna memilih Discard
                                setResult(RESULT_CANCELED);
                                finish();
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // Pengguna memilih Cancel, dialog ditutup, tidak melakukan apa-apa
                                dialog.dismiss();
                            })
                            .show();
                } else {
                    // Tidak ada perubahan, langsung kembali
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        };

        // Tambahkan callback ke dispatcher
        // Menggunakan 'this' sebagai LifecycleOwner memastikan callback otomatis dihapus saat Activity destroyed
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }
}