package com.example.iniapitest;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iniapitest.adapter.CharacterAdapter;
import com.example.iniapitest.model.Character;
import com.example.iniapitest.model.CharacterResponse;
import com.example.iniapitest.network.ApiService;
import com.example.iniapitest.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter adapter;
    private List<Character> characterList = new ArrayList<>();
    private int currentPage = 1;
    private final int MAX_PAGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CharacterAdapter(characterList);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(() -> {
            if (currentPage < MAX_PAGE) {
                currentPage++;
                loadCharacters(currentPage);
            } else {
                Toast.makeText(this, "Tidak ada data lagi", Toast.LENGTH_SHORT).show();
            }
        });

        loadCharacters(currentPage);
    }

    private void loadCharacters(int page) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getCharacters(page).enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
                if (response.isSuccessful()) {
                    List<Character> newCharacters = response.body().getResults();
                    characterList.addAll(newCharacters);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
