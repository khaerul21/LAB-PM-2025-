package com.example.tuprak;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class KataGantiEditActivity extends AppCompatActivity {

    private EditText kataGantiEditField;
    private ImageView backButton;
    private ImageView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kataganti_edit);

        kataGantiEditField = findViewById(R.id.kataGantiEditField);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        String currentKataGanti = getIntent().getStringExtra("current_kataganti");
        if (currentKataGanti != null && !currentKataGanti.isEmpty()) {
            kataGantiEditField.setText(currentKataGanti);
            kataGantiEditField.setSelection(currentKataGanti.length());
        }

        kataGantiEditField.requestFocus();

        backButton.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            saveKataGanti();
        });
    }

    private void saveKataGanti() {
        String newKataGanti = kataGantiEditField.getText().toString().trim();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_kataganti", newKataGanti);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}