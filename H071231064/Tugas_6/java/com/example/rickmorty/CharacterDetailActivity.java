package com.example.rickmorty;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterDetailActivity extends AppCompatActivity {

    private ImageView characterImage;
    private TextView characterName;
    private TextView characterStatusSpecies;
    private TextView characterGender;
    private TextView characterOrigin;
    private TextView characterLocation;
    private ProgressBar progressBar;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        characterImage = findViewById(R.id.character_image);
        characterName = findViewById(R.id.character_name);
        characterStatusSpecies = findViewById(R.id.character_status_species);
        characterGender = findViewById(R.id.character_gender);
        characterOrigin = findViewById(R.id.character_origin);
        characterLocation = findViewById(R.id.character_location);
        progressBar = findViewById(R.id.progress_bar);

        int characterId = getIntent().getIntExtra("character_id", -1);
        if (characterId != -1) {
            fetchCharacterDetails(characterId);
        }
    }

    private void fetchCharacterDetails(int characterId) {
        progressBar.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            RetrofitClient.getApi().getCharacter(characterId).enqueue(new Callback<RickMortyApi.Character>() {
                @Override
                public void onResponse(Call<RickMortyApi.Character> call, Response<RickMortyApi.Character> response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful() && response.body() != null) {
                            RickMortyApi.Character character = response.body();
                            characterName.setText(character.name);

                            if (character.status.equalsIgnoreCase("Alive")) {
                                findViewById(R.id.character_status_dot).setBackgroundResource(R.drawable.green_dot);
                            } else {
                                findViewById(R.id.character_status_dot).setBackgroundResource(R.drawable.red_dot);
                            }
                            characterStatusSpecies.setText(character.status + " - " + character.species);
                            
                            characterGender.setText(character.gender);
                            characterOrigin.setText(character.origin.name);
                            characterLocation.setText(character.location.name);
                            Glide.with(CharacterDetailActivity.this).load(character.image).into(characterImage);
                        }
                        progressBar.setVisibility(View.GONE);
                    });
                }

                @Override
                public void onFailure(Call<RickMortyApi.Character> call, Throwable t) {
                    runOnUiThread(() -> {
                        Log.e("CharacterDetail", "Error fetching character details", t);
                    });
                }
            });
        });
    }
}
