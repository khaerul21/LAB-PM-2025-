package com.example.tp_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EditPronounsActivity extends AppCompatActivity {

    private EditText pronounsEditText;
    private ImageView closeButton, checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pronouns);

        pronounsEditText = findViewById(R.id.pronouns);
        closeButton = findViewById(R.id.close);
        checkButton = findViewById(R.id.check);

        // Ambil dan tampilkan pronouns saat ini
        String currentPronouns = getIntent().getStringExtra("currentPronouns");
        if (currentPronouns != null) {
            pronounsEditText.setText(currentPronouns);
        }

        closeButton.setOnClickListener(v -> finish());

        checkButton.setOnClickListener(v -> {
            String newPronouns = pronounsEditText.getText().toString().trim();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedPronouns", newPronouns);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
