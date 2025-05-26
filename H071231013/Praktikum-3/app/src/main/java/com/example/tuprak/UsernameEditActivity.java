package com.example.tuprak;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class UsernameEditActivity extends AppCompatActivity {

    private EditText usernameEditField;
    private ImageView backButton;
    private ImageView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_edit);

        usernameEditField = findViewById(R.id.usernameEditField);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        String currentUsername = getIntent().getStringExtra("current_username");
        if (currentUsername != null && !currentUsername.isEmpty()) {
            usernameEditField.setText(currentUsername);
            usernameEditField.setSelection(currentUsername.length());
        }

        usernameEditField.requestFocus();

        backButton.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            saveUsername();
        });
    }

    private void saveUsername() {
        String newUsername = usernameEditField.getText().toString().trim();
        if (!newUsername.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_username", newUsername);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }
}