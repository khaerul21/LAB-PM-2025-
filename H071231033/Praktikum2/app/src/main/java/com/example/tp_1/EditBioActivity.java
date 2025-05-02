package com.example.tp_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EditBioActivity extends AppCompatActivity {

    private EditText bioEditText;
    private ImageView closeButton, checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);

        bioEditText = findViewById(R.id.bio);
        closeButton = findViewById(R.id.close);
        checkButton = findViewById(R.id.check);

        // Tampilkan bio saat ini
        String currentBio = getIntent().getStringExtra("currentBio");
        if (currentBio != null) {
            bioEditText.setText(currentBio);
        }

        closeButton.setOnClickListener(v -> finish());

        checkButton.setOnClickListener(v -> {
            String newBio = bioEditText.getText().toString().trim();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedBio", newBio);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
