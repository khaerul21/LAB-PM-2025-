package com.example.revisipraktikum02;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfile extends AppCompatActivity {
    private TextInputEditText etName, etID, passwordInput, emailInput;
    private ImageView imgProfile;
    private MaterialButton btnSave, btnDelete;
    private TextView tvAvatar;
    private String selectedImageUri;

    // Untuk memilih gambar
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImageUri = imageUri.toString();
                        imgProfile.setImageURI(imageUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inisialisasi view
        etName = findViewById(R.id.etName);
        etID = findViewById(R.id.etID);
        passwordInput = findViewById(R.id.passwordInput);
        emailInput = findViewById(R.id.emailInput);
        imgProfile = findViewById(R.id.imgProfile);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        tvAvatar = findViewById(R.id.tvAvatar);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        }

        // Ambil data user dari intent
        UserProfile user = getIntent().getParcelableExtra("USER_DATA");

        if (user == null) {
            user = new UserProfile(
                    "Chelsea Shelin P..",
                    "Celziii",
                    "12345",
                    "celzi@example.com",
                    ""
            );
        }

        if (user.getImageUri() != null && !user.getImageUri().isEmpty()) {
            selectedImageUri = user.getImageUri();
            imgProfile.setImageURI(Uri.parse(user.getImageUri()));
        } else {
            imgProfile.setImageResource(R.drawable.duolingo_char);
        }


        // Tombol Ganti Avatar
        tvAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
        });

        // Tombol Simpan
        btnSave.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String id = etID.getText() != null ? etID.getText().toString().trim() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";
            String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";

            UserProfile updatedUser = new UserProfile(
                    name.isEmpty() ? "Tidak diketahui" : name,
                    id.isEmpty() ? "Anonim" : id,
                    password,
                    email,
                    selectedImageUri != null ? selectedImageUri : ""
            );

            // Kirim data kembali ke MainActivity
            Intent intent = new Intent(EditProfile.this, Profile.class);
            intent.putExtra("UPDATED_USER", updatedUser);
            setResult(RESULT_OK, intent);
//            startActivity(intent);
            finish();
        });



        // Tombol Hapus Akun (opsional)
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur hapus akun belum diimplementasikan.", Toast.LENGTH_SHORT).show();
        });

    }


}


//public class EditProfile extends AppCompatActivity {
//
//    private TextInputEditText etName, etID, passwordInput, emailInput;
//    private ImageView imgProfile;
//    private TextView tvAvatar;
//    private MaterialButton btnSave, btnDelete;
//
//    private String selectedImageUri;
//
//    private final ActivityResultLauncher<Intent> pickImageLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    Uri imageUri = result.getData().getData();
//                    if (imageUri != null) {
//                        selectedImageUri = imageUri.toString();
//                        imgProfile.setImageURI(imageUri);
//                    }
//                }
//            });
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_edit_profile);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // Inisialisasi View
//        etName = findViewById(R.id.etName);
//        etID = findViewById(R.id.etID);
//        passwordInput = findViewById(R.id.passwordInput);
//        emailInput = findViewById(R.id.emailInput);
//        imgProfile = findViewById(R.id.imgProfile);
//        tvAvatar = findViewById(R.id.tvAvatar);
//        btnSave = findViewById(R.id.btnSave);
//        btnDelete = findViewById(R.id.btnDelete);
//
//        // Setup Toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
//        }
//
//        // Ambil data dari MenuSetting
//        UserProfile user = getIntent().getParcelableExtra("USER_DATA");
//
//        if (user != null) {
//            etName.setText(user.getName());
//            etID.setText(user.getUserId());
//            passwordInput.setText(user.getPassword());
//            emailInput.setText(user.getEmail());
//
//            if (user.getImageUri() != null && !user.getImageUri().isEmpty()) {
//                selectedImageUri = user.getImageUri();
//                imgProfile.setImageURI(Uri.parse(user.getImageUri()));
//            }
//        }
//
//        // Tombol Simpan
//        btnSave.setOnClickListener(v -> {
//            String name = etName.getText().toString().trim();
//            String id = etID.getText().toString().trim();
//            String password = passwordInput.getText().toString().trim();
//            String email = emailInput.getText().toString().trim();
//
//            if (name.isEmpty() || id.isEmpty() || password.isEmpty() || email.isEmpty()) {
//                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            UserProfile updatedUser = new UserProfile(name, id, password, email, selectedImageUri);
//
//            Intent intent = new Intent(EditProfile.this, Profile.class);
//            intent.putExtra("UPDATED_USER", updatedUser);
//            startActivity(intent);
//            finish();
//        });
//    }
//}
