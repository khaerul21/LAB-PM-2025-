package com.example.t6_prak;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t6_prak.adapter.CharAdapter;
import com.example.t6_prak.adapter.CharAdapter;
import com.example.t6_prak.model.Character;
import com.example.t6_prak.model.CharacterResponse;
import com.example.t6_prak.network.ApiService;
import com.example.t6_prak.network.OnLoadMoreListener;
import com.example.t6_prak.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CharAdapter adapter;
    private List<Character> characterList = new ArrayList<>();
    private int currentPage = 1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == characterList.size()) ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CharAdapter(characterList);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMoreClicked() {
                loadMoreCharacters();
            }
        });
        recyclerView.setAdapter(adapter);

        // Memuat data awal
        loadCharacters();
    }

    private void loadCharacters() {
        // Tampilkan loading jika ini adalah halaman pertama
        if (currentPage == 1) {
            progressBar.setVisibility(View.VISIBLE);
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<CharacterResponse> call = apiService.getCharacters(currentPage);

        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
                // Sembunyikan loading
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Character> newCharacters = response.body().getResults();
                    characterList.addAll(newCharacters);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                // Sembunyikan loading
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreCharacters() {
        currentPage++;
        loadCharacters();
    }
}