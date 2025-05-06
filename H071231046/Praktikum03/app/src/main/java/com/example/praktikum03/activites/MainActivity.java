package com.example.praktikum03.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum03.R;
import com.example.praktikum03.adapters.FeedAdapter;
import com.example.praktikum03.utils.DataSource;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFeed;
    //    private FeedAdapter feedAdapter;
    private ImageView ic_home, ic_post;
    private CircleImageView ic_profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewFeed = findViewById(R.id.recyclerViewFeed);
        ic_home = findViewById(R.id.ic_home);
        ic_post= findViewById(R.id.ic_post);
        ic_profile = findViewById(R.id.ic_profile);

        recyclerViewFeed.setLayoutManager(new LinearLayoutManager(this));// Set layout manager

        FeedAdapter feedAdapter = new FeedAdapter(this, DataSource.getFeedPostList());
        recyclerViewFeed.setAdapter(feedAdapter);
        Log.d("MainActivity", "Adapter set dengan item count: " + feedAdapter.getItemCount());

        ic_home.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        ic_post.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
            startActivity(intent);
        });

        ic_profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        Log.d("MainActivity", "Jumlah feed: " + DataSource.getFeedPostList().size());
    }
}