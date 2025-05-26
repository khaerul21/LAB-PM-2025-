package com.example.tuprak;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class BioEditActivity extends AppCompatActivity {

    private EditText bioEditField;
    private ImageView closeButton;
    private ImageView saveButton;
    private TextView charCounterText;
    private final int MAX_BIO_LENGTH = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_edit);

        bioEditField = findViewById(R.id.bioEditField);
        closeButton = findViewById(R.id.closeButton);
        saveButton = findViewById(R.id.saveButton);
        charCounterText = findViewById(R.id.charCounterText);

        String currentBio = getIntent().getStringExtra("current_bio");
        if (currentBio != null) {
            bioEditField.setText(currentBio);
            updateCharCounter(currentBio.length());
            bioEditField.setSelection(currentBio.length());
        } else {
            updateCharCounter(0);
        }

        bioEditField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                updateCharCounter(s.length());
            }
        });

        bioEditField.requestFocus();

        closeButton.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            saveBio();
        });
    }

    private void updateCharCounter(int currentLength) {
        charCounterText.setText(currentLength + "/" + MAX_BIO_LENGTH);
        
        if (currentLength > MAX_BIO_LENGTH) {
            saveButton.setAlpha(0.5f);
            saveButton.setEnabled(false);
        } else {
            saveButton.setAlpha(1.0f);
            saveButton.setEnabled(true);
        }
    }

    private void saveBio() {
        String newBio = bioEditField.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_bio", newBio);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}