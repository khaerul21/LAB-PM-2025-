package com.example.tp_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditNameActivity extends AppCompatActivity {

    private EditText nameEditText;
    private ImageView closeButton, checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        nameEditText = findViewById(R.id.name);
        closeButton = findViewById(R.id.close);
        checkButton = findViewById(R.id.check);

        // close
        closeButton.setOnClickListener(v -> finish());

        // ambil nama yang lama
        Intent intent = getIntent();
        String currentName = intent.getStringExtra("currentName");
        if (currentName != null) {
            nameEditText.setText(currentName);
        }

        // Mengupdate nama kalo tombol check diklik
        checkButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(EditNameActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                // Kirim kembali nama yang telah diperbarui ke EditProfileActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedName", newName);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
