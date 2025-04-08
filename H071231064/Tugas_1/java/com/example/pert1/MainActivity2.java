package com.example.pert1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btn = findViewById(R.id.btn_back);
        Intent pindah = new Intent(MainActivity2.this, MainActivity.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(pindah);
            }
        });

        Button btnParse = findViewById(R.id.btn_parse);
        Intent pindahParse = new Intent(MainActivity2.this, MainActivity3.class);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = "A.M.Yusran Mazidan";
                String nim = "H071231064";
                String ang = "2023";
                String imgPath = "https://picsum.photos/200";

                pindahParse.putExtra(MainActivity3.EXTRA_NAMA, nama);
                pindahParse.putExtra(MainActivity3.EXTRA_NIM, nim);
                pindahParse.putExtra(MainActivity3.EXTRA_ANG, ang);
                pindahParse.putExtra(MainActivity3.EXTRA_IMAGE_PATH, imgPath);
                startActivity(pindahParse);
            }
        });
    }
}