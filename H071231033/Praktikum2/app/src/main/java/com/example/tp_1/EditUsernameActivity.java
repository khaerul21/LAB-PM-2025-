package com.example.tp_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditUsernameActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private ImageView closeButton, checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_username);

        usernameEditText = findViewById(R.id.username);
        closeButton = findViewById(R.id.close);
        checkButton = findViewById(R.id.check);

        // Menampilkan username saat ini
        Intent intent = getIntent();
        String currentUsername = intent.getStringExtra("currentUsername");
        if (currentUsername != null) {
            usernameEditText.setText(currentUsername);
        }

        closeButton.setOnClickListener(v -> finish());

        checkButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString().trim();
            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedUsername", newUsername);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
