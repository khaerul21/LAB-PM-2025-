package com.example.iniapitest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView nameTextView, statusTextView, speciesTextView, genderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detailImageView);
        nameTextView = findViewById(R.id.detailNameTextView);
        statusTextView = findViewById(R.id.detailStatusTextView);
        speciesTextView = findViewById(R.id.detailSpeciesTextView);
        genderTextView = findViewById(R.id.detailGenderTextView);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");
        String species = intent.getStringExtra("species");
        String gender = intent.getStringExtra("gender");
        String image = intent.getStringExtra("image");

        nameTextView.setText(name);
        statusTextView.setText(status);
        speciesTextView.setText(species);
        genderTextView.setText(gender);
        Picasso.get().load(image).into(imageView);
    }
}
