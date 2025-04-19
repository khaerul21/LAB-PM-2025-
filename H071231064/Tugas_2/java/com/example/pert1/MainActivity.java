package com.example.pert1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_NAMA = "EXTRA_NAMA";
    public static String EXTRA_NIM = "EXTRA_NIM";
    public static String EXTRA_ANG = "EXTRA_ANG";
    public static String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    private TextView textUsername, textNim, textAngkatan;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linkedin);

        textUsername = findViewById(R.id.text_username);
        textNim = findViewById(R.id.text_nim);
        textAngkatan = findViewById(R.id.text_angkatan);
        imageView = findViewById(R.id.profileImage);

        String nama = getIntent().getStringExtra(EXTRA_NAMA);
        String nim = getIntent().getStringExtra(EXTRA_NIM);
        String angkatan = getIntent().getStringExtra(EXTRA_ANG);
        String imgPath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);

        textUsername.setText(nama);
        textNim.setText(nim);
        textAngkatan.setText(angkatan);

        if (imgPath != null && imgPath.startsWith("content://")) {
            imageView.setImageURI(Uri.parse(imgPath));
        } else {
            imageView.setImageURI(Uri.parse("https://picsum.photos/200"));
        }

        Button btnEditProfil = findViewById(R.id.btn_edit_profil);
        btnEditProfil.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra(EXTRA_NAMA, nama);
            intent.putExtra(EXTRA_NIM, nim);
            intent.putExtra(EXTRA_ANG, angkatan);
            intent.putExtra(EXTRA_IMAGE_PATH, imgPath);
            startActivity(intent);
        });
    }
}