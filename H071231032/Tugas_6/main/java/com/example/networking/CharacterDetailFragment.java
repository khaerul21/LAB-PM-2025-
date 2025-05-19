package com.example.networking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CharacterDetailFragment extends Fragment {

    private static final String ARG_CHARACTER_ID = "character_id_arg";
    private ImageView detailCharacterImage;
    private TextView detailCharacterName, detailCharacterStatus, detailCharacterSpecies, detailCharacterGender,
            buttonTryAgainDetail, labelStatus, labelSpecies,labelGender, tvError;
    private ProgressBar progressBarDetail;
    private int characterId;
    private Character character;
    private ApiService apiService;
    private ExecutorService executorService;

    public static CharacterDetailFragment newInstance(int characterId) {
        CharacterDetailFragment fragment = new CharacterDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHARACTER_ID, characterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            characterId = getArguments().getInt(ARG_CHARACTER_ID);
        }
        executorService = Executors.newSingleThreadExecutor();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_detail, container, false);

        buttonTryAgainDetail = view.findViewById(R.id.button_try_again_detail);
        buttonTryAgainDetail.setOnClickListener(v -> fetchCharacterDetail(characterId));

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Detail Character");
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        }

        detailCharacterImage = view.findViewById(R.id.detail_character_image);
        detailCharacterName = view.findViewById(R.id.detail_character_name);
        detailCharacterStatus = view.findViewById(R.id.detail_character_status);
        detailCharacterSpecies = view.findViewById(R.id.detail_character_species);
        detailCharacterGender = view.findViewById(R.id.detail_character_gender);
        labelStatus = view.findViewById(R.id.label_status);
        labelSpecies = view.findViewById(R.id.label_species);
        labelGender = view.findViewById(R.id.label_gender);
        tvError = view.findViewById(R.id.text_view_error);
        progressBarDetail = view.findViewById(R.id.progress_bar_detail);

        fetchCharacterDetail(characterId);

        return view;
    }

    private void fetchCharacterDetail(int id) {
        tvError.setVisibility(View.GONE);
        buttonTryAgainDetail.setVisibility(View.GONE);
        progressBarDetail.setVisibility(View.VISIBLE);
        detailCharacterImage.setVisibility(View.GONE);
        detailCharacterName.setVisibility(View.GONE);
        detailCharacterStatus.setVisibility(View.GONE);
        detailCharacterSpecies.setVisibility(View.GONE);
        detailCharacterGender.setVisibility(View.GONE);
        labelStatus.setVisibility(View.GONE);
        labelSpecies.setVisibility(View.GONE);
        labelGender.setVisibility(View.GONE);

        executorService.execute(() -> {
            try {
                retrofit2.Response<Character> response = apiService.getCharacterDetail(id).execute();
                requireActivity().runOnUiThread(() -> {
                    progressBarDetail.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        character = response.body();
                        displayCharacterDetails(character);
                    } else {
                        showError("Failed to load character detail");
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    progressBarDetail.setVisibility(View.GONE);
                    showError("Error: No Internet Connection");
                });
            }
        });
    }

    private void showError(String errorMessage) {
        tvError.setText(errorMessage);
        tvError.setVisibility(View.VISIBLE);
        buttonTryAgainDetail.setVisibility(View.VISIBLE);
    }

    private void displayCharacterDetails(Character charData) {
        tvError.setVisibility(View.GONE);
        buttonTryAgainDetail.setVisibility(View.GONE);
        detailCharacterImage.setVisibility(View.VISIBLE);
        detailCharacterName.setVisibility(View.VISIBLE);
        detailCharacterStatus.setVisibility(View.VISIBLE);
        detailCharacterSpecies.setVisibility(View.VISIBLE);
        detailCharacterGender.setVisibility(View.VISIBLE);
        labelStatus.setVisibility(View.VISIBLE);
        labelSpecies.setVisibility(View.VISIBLE);
        labelGender.setVisibility(View.VISIBLE);

        detailCharacterName.setText(charData.getName());
        detailCharacterStatus.setText(charData.getStatus());
        detailCharacterSpecies.setText(charData.getSpecies());
        detailCharacterGender.setText(charData.getGender());

        Glide.with(this)
                .load(charData.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(detailCharacterImage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detailCharacterImage = null;
        detailCharacterName = null;
        detailCharacterGender = null;
        detailCharacterSpecies = null;
        detailCharacterStatus = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}