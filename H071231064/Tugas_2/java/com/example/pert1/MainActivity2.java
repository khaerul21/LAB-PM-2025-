package com.example.pert1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private EditText editNama, editNim, editAngkatan;
    private Button btnPickImage;
    private ImageView imagePreview;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editNama = findViewById(R.id.edit_nama);
        editNim = findViewById(R.id.edit_nim);
        editAngkatan = findViewById(R.id.edit_angkatan);
        btnPickImage = findViewById(R.id.btn_pick_image);
        imagePreview = findViewById(R.id.image_preview);

        Intent intent = getIntent();
        String nama = intent.getStringExtra(MainActivity.EXTRA_NAMA);
        String nim = intent.getStringExtra(MainActivity.EXTRA_NIM);
        String angkatan = intent.getStringExtra(MainActivity.EXTRA_ANG);
        String imgPath = intent.getStringExtra(MainActivity.EXTRA_IMAGE_PATH);

        editNama.setText(nama);
        editNim.setText(nim);
        editAngkatan.setText(angkatan);

        if (imgPath != null && !imgPath.isEmpty()) {
            imageUri = Uri.parse(imgPath);
            imagePreview.setImageURI(imageUri);
        }

        btnPickImage.setOnClickListener(v -> openFileChooser());

        Button btnParse = findViewById(R.id.btn_parse);
        btnParse.setOnClickListener(v -> {

            String updatedNama = editNama.getText().toString().trim();
            String updatedNim = editNim.getText().toString().trim();
            String updatedAngkatan = editAngkatan.getText().toString().trim();
            String imgPathUpdated = (imageUri != null) ? imageUri.toString() : "https://picsum.photos/200";

            if (TextUtils.isEmpty(updatedNama)) updatedNama = "A.M.Yusran Mazidan";
            if (TextUtils.isEmpty(updatedNim)) updatedNim = "H071231064";
            if (TextUtils.isEmpty(updatedAngkatan)) updatedAngkatan = "2023";

            Intent intentBack = new Intent(MainActivity2.this, MainActivity.class);
            intentBack.putExtra(MainActivity.EXTRA_NAMA, updatedNama);
            intentBack.putExtra(MainActivity.EXTRA_NIM, updatedNim);
            intentBack.putExtra(MainActivity.EXTRA_ANG, updatedAngkatan);
            intentBack.putExtra(MainActivity.EXTRA_IMAGE_PATH, imgPathUpdated);
            startActivity(intentBack);
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
        }
    }
}