package com.example.rickmorty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private CharacterAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CharacterAdapter(new ArrayList<>(), characterId -> {
            Intent intent = new Intent(MainActivity.this, CharacterDetailActivity.class);
            intent.putExtra("character_id", characterId);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        fetchCharacters();
                    }
                }
            }
        });

        fetchCharacters();
    }

    private void fetchCharacters() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            RetrofitClient.getApi().getCharacters(currentPage).enqueue(new Callback<RickMortyApi.CharacterResponse>() {
                @Override
                public void onResponse(Call<RickMortyApi.CharacterResponse> call, Response<RickMortyApi.CharacterResponse> response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful() && response.body() != null) {
                            List<RickMortyApi.Character> characters = response.body().results;
                            adapter.addCharacters(characters);

                            currentPage++;
                            isLastPage = response.body().info.next == null;
                        }
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    });
                }

                @Override
                public void onFailure(Call<RickMortyApi.CharacterResponse> call, Throwable t) {
                    runOnUiThread(() -> {
                        Log.e("MainActivity", "Error fetching characters", t);
                        isLoading = false;
                    });
                }
            });
        });
    }
}