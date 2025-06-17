package com.example.praktikum06_1.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.praktikum06_1.R;
import com.example.praktikum06_1.network.ApiConfig;
import com.example.praktikum06_1.network.ApiService;
import com.example.praktikum06_1.response.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private ImageView image, backBtn, refreshBtn;
    private TextView name, species, gender, status, pesan;
    private ProgressBar progressBar;
//    private LinearLayout dataLayout, errorLayout;
    private View dataLayout, errorLayout;

    public static DetailFragment newInstance(int characterId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("characterId", characterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        image = view.findViewById(R.id.image);
        backBtn = view.findViewById(R.id.back);
        refreshBtn = view.findViewById(R.id.refresh);
        name = view.findViewById(R.id.name);
        species = view.findViewById(R.id.species);
        gender = view.findViewById(R.id.gender);
        status = view.findViewById(R.id.status);
        progressBar = view.findViewById(R.id.pb);
        errorLayout = view.findViewById(R.id.errorLayout);
        dataLayout = view.findViewById(R.id.data);
        pesan = view.findViewById(R.id.pesan);

        int characterId = getArguments().getInt("characterId", -1);
        showLoadingState();
        getCharacterById(characterId);

        backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        refreshBtn.setOnClickListener(v -> {
            showLoadingState();
            getCharacterById(characterId);
        });

        return view;
    }

    private void getCharacterById(int characterId) {
        ApiService apiService = ApiConfig.getClient().create(ApiService.class);
        apiService.getCharacterById(characterId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    showContentState();
                    displayCharacterDetails(user);
                } else {
                    showErrorState();
                    Toast.makeText(requireContext(), "Gagal memuat data karakter", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showErrorState();
                Toast.makeText(requireContext(), "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoadingState() {
        progressBar.setVisibility(View.VISIBLE);
        dataLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    private void showContentState() {
        progressBar.setVisibility(View.GONE);
        dataLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    private void showErrorState() {
        progressBar.setVisibility(View.GONE);
        dataLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void displayCharacterDetails(User user) {
        if (user != null) {
            name.setText(user.getName());
            species.setText(user.getSpecies());
            gender.setText(user.getGender());
            status.setText(user.getStatus());
            Glide.with(requireContext()).load(user.getImage()).into(image);
        }
    }
}
