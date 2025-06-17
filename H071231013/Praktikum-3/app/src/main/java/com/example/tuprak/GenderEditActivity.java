package com.example.tuprak;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GenderEditActivity extends AppCompatActivity {

    private RadioGroup genderRadioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioCustom;
    private RadioButton radioPreferNotToSay;
    private ImageView closeButton;
    private ImageView saveButton;
    private LinearLayout customGenderContainer;
    private EditText customGenderInput;
    private TextView customGenderError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_edit);

        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioCustom = findViewById(R.id.radioCustom);
        radioPreferNotToSay = findViewById(R.id.radioPreferNotToSay);
        closeButton = findViewById(R.id.closeButton);
        saveButton = findViewById(R.id.saveButton);
        customGenderContainer = findViewById(R.id.customGenderContainer);
        customGenderInput = findViewById(R.id.customGenderInput);
        customGenderError = findViewById(R.id.customGenderError);

        String currentGender = getIntent().getStringExtra("current_gender");
        if (currentGender != null) {
            selectRadioButtonByGender(currentGender);
        } else {
            radioMale.setChecked(true);
        }

        closeButton.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            if (radioCustom.isChecked() && customGenderInput.getText().toString().trim().isEmpty()) {
                customGenderError.setVisibility(View.VISIBLE);
                return;
            }
            saveGender();
        });

        radioCustom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                customGenderContainer.setVisibility(View.VISIBLE);
                if (customGenderInput.getText().toString().trim().isEmpty()) {
                    customGenderError.setVisibility(View.VISIBLE);
                }
            } else {
                customGenderContainer.setVisibility(View.GONE);
                customGenderError.setVisibility(View.GONE);
            }
        });

        genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != R.id.radioCustom) {
                customGenderContainer.setVisibility(View.GONE);
                customGenderError.setVisibility(View.GONE);
            }
        });

        customGenderInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    customGenderError.setVisibility(View.VISIBLE);
                } else {
                    customGenderError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void selectRadioButtonByGender(String gender) {
        if (gender.equalsIgnoreCase("Laki-laki")) {
            radioMale.setChecked(true);
        } else if (gender.equalsIgnoreCase("Perempuan")) {
            radioFemale.setChecked(true);
        } else if (gender.equalsIgnoreCase("Memilih untuk tidak memberi tahu")) {
            radioPreferNotToSay.setChecked(true);
        } else if (gender.equalsIgnoreCase("Khusus")) {
            radioCustom.setChecked(true);
            customGenderContainer.setVisibility(View.VISIBLE);
        } else {
            radioCustom.setChecked(true);
            customGenderContainer.setVisibility(View.VISIBLE);
            customGenderInput.setText(gender);
        }
    }

    private void saveGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String selectedGender = "";

        if (selectedId == R.id.radioMale) {
            selectedGender = "Laki-laki";
        } else if (selectedId == R.id.radioFemale) {
            selectedGender = "Perempuan";
        } else if (selectedId == R.id.radioCustom) { 
            String customGender = customGenderInput.getText().toString().trim();
            if (!customGender.isEmpty()) {
                selectedGender = customGender;
            } else {
                selectedGender = "Khusus";
            }
        } else if (selectedId == R.id.radioPreferNotToSay) {
            selectedGender = "Memilih untuk tidak memberi tahu"; 
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_gender", selectedGender);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Selected gender: " + selectedGender, Toast.LENGTH_SHORT).show();

        finish();
    }
}