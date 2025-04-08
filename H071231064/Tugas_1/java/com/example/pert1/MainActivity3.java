package com.example.pert1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity3 extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private ImageView imageView;
    public static final String EXTRA_NAMA = "extra_nama";
    public static final String EXTRA_NIM = "extra_nim";
    public static final String EXTRA_ANG = "extra_ang";
    public static final String EXTRA_IMAGE_PATH = "extra-image_path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button btn = findViewById(R.id.btn_back);
        Intent pindah = new Intent(MainActivity3.this, MainActivity.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(pindah);
            }
        });

        tv1 = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        imageView = findViewById(R.id.imageView);

        String name = getIntent().getStringExtra(EXTRA_NAMA);
        tv1.setText(name);

        String nim = getIntent().getStringExtra(EXTRA_NIM);
        tv2.setText(nim);

        String angkatan = getIntent().getStringExtra(EXTRA_ANG);
        tv3.setText(angkatan);

        String uriString = getIntent().getStringExtra(EXTRA_IMAGE_PATH);

        Uri uriGambar = Uri.parse(uriString);
        Log.i("MainActivity",uriString);
        imageView.setImageURI(uriGambar);
    }
}