package com.example.t6_prak;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView imageView = findViewById(R.id.detailImageView);
        TextView nameTextView = findViewById(R.id.detailNameTextView);
        TextView statusTextView = findViewById(R.id.detailStatusTextView);
        TextView speciesTextView = findViewById(R.id.detailSpeciesTextView);
        TextView genderTextView = findViewById(R.id.detailGenderTextView);

        String name = getIntent().getStringExtra("name");
        String status = getIntent().getStringExtra("status");
        String species = getIntent().getStringExtra("species");
        String gender = getIntent().getStringExtra("gender");
        String image = getIntent().getStringExtra("image");

        nameTextView.setText(name);
        statusTextView.setText(status);
        speciesTextView.setText(species);
        genderTextView.setText(gender);
        Picasso.get().load(image).into(imageView);

        // Tambahkan listener untuk kembali ke halaman sebelumnya saat klik image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}