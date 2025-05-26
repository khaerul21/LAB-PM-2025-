package com.example.tuprak;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class NameEditActivity extends AppCompatActivity {

    private EditText nameEditField;
    private ImageView backButton;
    private ImageView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_edit);

        nameEditField = findViewById(R.id.nameEditField);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        String currentName = getIntent().getStringExtra("current_name");
        if (currentName != null && !currentName.isEmpty()) {
            nameEditField.setText(currentName);
            nameEditField.setSelection(currentName.length());
        }

        nameEditField.requestFocus();

        backButton.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            saveName();
        });
    }

    private void saveName() {
        String newName = nameEditField.getText().toString().trim();
        if (!newName.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_name", newName);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }
}