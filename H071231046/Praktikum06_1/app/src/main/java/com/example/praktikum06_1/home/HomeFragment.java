package com.example.praktikum06_1.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.praktikum06_1.R;
import com.example.praktikum06_1.network.ApiConfig;
import com.example.praktikum06_1.network.ApiService;
import com.example.praktikum06_1.network.NetworkUtil;
import com.example.praktikum06_1.response.User;
import com.example.praktikum06_1.response.UserResponse;
import com.example.praktikum06_1.ui.UserAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private ImageView refreshBtn;
    private TextView pesan;
    private UserAdapter userAdapter;
    private int currentPage = 1;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.userRecycler);
        progressBar = view.findViewById(R.id.pb);
        errorLayout = view.findViewById(R.id.errorLayout);
        refreshBtn = view.findViewById(R.id.refresh);
        pesan = view.findViewById(R.id.pesan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(new ArrayList<>());
        recyclerView.setAdapter(userAdapter);

        userAdapter.setOnUserClickListener(user -> {
            int characterId = user.getId();
            DetailFragment detailFragment = DetailFragment.newInstance(characterId);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        refreshBtn.setOnClickListener(v -> loadUsers(currentPage));

        userAdapter.setOnLoadMoreClickListener(() -> {
            if (!isLoading) {
                currentPage++;
                loadUsers(currentPage);
            }
        });

        loadUsers(currentPage);
        return view;
    }

    private void loadUsers(int page) {
        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            showError(true, "Gagal memuat data");
            return;
        }

        showError(false, null);
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiConfig.getClient().create(ApiService.class);
        apiService.getCharacterData(page).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<User> newUsers = response.body().getResults();
                    if (page == 1) userAdapter.clearUsers();
                    userAdapter.addMoreUsers(newUsers);
                    userAdapter.setShowLoadMore(!newUsers.isEmpty());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                showError(true, "Gagal memuat data.\n" + t.getMessage());
            }
        });
    }

    private void showError(boolean show, String message) {
        errorLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        if (message != null) pesan.setText(message);
    }
}
