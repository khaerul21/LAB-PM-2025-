package com.example.revisipraktikum02;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MenuSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
0
        Button btnSettingProfil = findViewById(R.id.settingprofil);
        //Terima UserProfile dari MainActivity
        UserProfile user = getIntent().getParcelableExtra("USER_DATA");


        btnSettingProfil.setOnClickListener(v -> {
            Intent intent = new Intent(MenuSetting.this, EditProfile.class);
            //kirim userprofile ke editprofile
            intent.putExtra("USER_DATA", user);
            startActivityForResult(intent, 1); // ID request bebas
        });

        TextView txtBack = findViewById(R.id.txtBack);

        txtBack.setOnClickListener(v -> {
            Intent intent = new Intent(MenuSetting.this, MainActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Terima UserProfile yang telah diperbarui dari EditProfile
            UserProfile updatedUser = data.getParcelableExtra("UPDATED_USER");
            if (updatedUser != null) {
                Intent backIntent = new Intent();
                backIntent.putExtra("UPDATED_USER", updatedUser);
                setResult(RESULT_OK, backIntent);
                finish(); // balik ke MainActivity
            }
        }
    }

}