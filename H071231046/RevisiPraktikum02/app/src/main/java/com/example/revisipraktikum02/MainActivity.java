package com.example.revisipraktikum02;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private ImageView imgProfile;
    private TextView tvNama, tvId, tvEmail;

    private UserProfile currentUser;

    private final ActivityResultLauncher<Intent> menuSettingLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    UserProfile updatedUser = result.getData().getParcelableExtra("UPDATED_USER");
                    if (updatedUser != null) {
                        currentUser = updatedUser;
                        updateUI(currentUser);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inset layout untuk edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile);
        tvNama = findViewById(R.id.tvNama);
        tvId = findViewById(R.id.tvId);
        tvEmail = findViewById(R.id.tvEmail);

        // Dummy data default saat pertama kali
        currentUser = new UserProfile("Chelsea Shelin P..", "Celziii", "12345", "celzi@example.com", null);
        updateUI(currentUser);

        // Tombol setting ke MenuSetting
        ImageButton btnSetting = findViewById(R.id.setting_icon);
        btnSetting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuSetting.class);
            //kirim userprofile ke menusetting
            intent.putExtra("USER_DATA", currentUser);
            menuSettingLauncher.launch(intent);
        });
    }

    private void updateUI(UserProfile user) {
        tvNama.setText(user.getName());
        tvId.setText(user.getUserId());
        tvEmail.setText(user.getEmail());

        try {
            if (user.getImageUri() != null && !user.getImageUri().isEmpty()) {
                imgProfile.setImageURI(Uri.parse(user.getImageUri()));
            } else {
                imgProfile.setImageResource(R.drawable.duolingo_char);
            }
        } catch (Exception e) {
            imgProfile.setImageResource(R.drawable.duolingo_char);
            e.printStackTrace();
        }

    }

    // Terima intent baru dari Profile.java
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // Update UI jika data kembali dari Profile
    @Override
    protected void onResume() {
        super.onResume();

        //terima userprofile hasil update
        UserProfile updatedUser = getIntent().getParcelableExtra("UPDATED_USER");
        if (updatedUser != null) {
            currentUser = updatedUser;
            updateUI(updatedUser);
        }
    }
}
