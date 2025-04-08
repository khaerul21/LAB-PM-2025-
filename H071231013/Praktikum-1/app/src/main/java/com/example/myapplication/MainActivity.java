package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi RecyclerView
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);

        // Setup RecyclerView
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(postAdapter);

        // Tambahkan data postingan
        addSamplePosts();
    }

    private void addSamplePosts() {
        postList.add(new Post("seblak bukan makanan manusia", 247, 499, 2600));
        postList.add(new Post("kangen ayam geprek", 43, 140, 139));
        postList.add(new Post("kangen es teh + mie ayam", 17, 29, 112));
        postList.add(new Post("kangen stv", 0, 0, 0));
        postAdapter.notifyDataSetChanged();
    }
}