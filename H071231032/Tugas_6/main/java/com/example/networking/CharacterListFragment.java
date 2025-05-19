package com.example.networking;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Response;

public class CharacterListFragment extends Fragment implements CharacterAdapter.OnItemClickListener {

    private static final String TAG = "CharacterListFragment";
    private RecyclerView recyclerViewCharacters;
    private CharacterAdapter characterAdapter;
    private ProgressBar progressBarList;
    private TextView textViewError;
    private TextView buttonTryAgain;
    private Button buttonLoadMore;

    private ApiService apiService;
    private ExecutorService executorService;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private int currentPage = 1;
    private String nextPageUrl = null;
    private boolean isLoading = false;
    private List<Character> allCharacters = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getClient().create(ApiService.class);
        executorService = Executors.newSingleThreadExecutor();
        characterAdapter = new CharacterAdapter(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_list, container, false);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Rick and Morty Characters");
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }


        recyclerViewCharacters = view.findViewById(R.id.recycler_view_characters);
        progressBarList = view.findViewById(R.id.progress_bar_list);
        textViewError = view.findViewById(R.id.text_view_error);
        buttonTryAgain = view.findViewById(R.id.button_try_again);
        buttonLoadMore = view.findViewById(R.id.button_load_more);

        setupRecyclerView();

        buttonTryAgain.setOnClickListener(v -> fetchCharacters(currentPage));
        buttonLoadMore.setOnClickListener(v -> {
            if (nextPageUrl != null && !isLoading) {
                fetchCharactersFromUrl(nextPageUrl);
            }
        });

        recyclerViewCharacters.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == characterAdapter.getItemCount() - 1) {
                        // Tampilkan tombol "Load More" hanya jika ada halaman berikutnya dan tidak sedang loading
                        if (nextPageUrl != null && !isLoading) {
                            buttonLoadMore.setVisibility(View.VISIBLE);
                        } else {
                            buttonLoadMore.setVisibility(View.GONE);
                        }
                    } else {
                        buttonLoadMore.setVisibility(View.GONE);
                    }
                }
            }
        });

        if (allCharacters.isEmpty()) { // Hanya fetch jika data kosong (misalnya saat pertama kali fragment dibuat)
            fetchCharacters(currentPage);
        } else {
            characterAdapter.setCharacters(allCharacters);
            if (nextPageUrl != null) {
                buttonLoadMore.setVisibility(View.VISIBLE);
            } else {
                buttonLoadMore.setVisibility(View.GONE);
            }
        }

        return view;
    }

    private void setupRecyclerView() {
        recyclerViewCharacters.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCharacters.setAdapter(characterAdapter);
    }

    private void fetchCharacters(int page) {
        showLoading(true);
        isLoading = true;
        buttonLoadMore.setVisibility(View.GONE);
        recyclerViewCharacters.setVisibility(View.GONE);
        textViewError.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);

        executorService.execute(() -> {
            Call<ApiResponse> call = apiService.getCharacters(page);
            try {
                Response<ApiResponse> response = call.execute(); // Eksekusi sinkron di background thread
                mainThreadHandler.post(() -> { // Kembali ke main thread untuk update UI
                    showLoading(false);
                    isLoading = false;
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        if (page == 1) { // Data awal
                            allCharacters.clear();
                            allCharacters.addAll(apiResponse.getResults());
                            characterAdapter.setCharacters(allCharacters);
                        } else { // Load more
                            allCharacters.addAll(apiResponse.getResults());
                            characterAdapter.addCharacters(apiResponse.getResults());
                        }

                        if (apiResponse.getInfo() != null && apiResponse.getInfo().getNext() != null) {
                            nextPageUrl = apiResponse.getInfo().getNext();
                            buttonLoadMore.setVisibility(View.VISIBLE);
                        } else {
                            nextPageUrl = null;
                            buttonLoadMore.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "All characters loaded", Toast.LENGTH_SHORT).show();
                        }
                        currentPage++; // Increment untuk request page berikutnya jika menggunakan nomor page
                    } else {
                        handleApiError("Failed to fetch data. Code: " + response.code());
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching characters");
                mainThreadHandler.post(() -> {
                    showLoading(false);
                    isLoading = false;
                    handleApiError("Error: No Internet Connection");
                });
            }
        });
    }

    private void fetchCharactersFromUrl(String url) {
        showLoading(true);
        isLoading = true;
        buttonLoadMore.setVisibility(View.GONE);
        textViewError.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);

        executorService.execute(() -> {
            Call<ApiResponse> call = apiService.getCharactersFromUrl(url);
            try {
                Response<ApiResponse> response = call.execute();
                mainThreadHandler.post(() -> {
                    showLoading(false);
                    isLoading = false;
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        allCharacters.addAll(apiResponse.getResults());
                        characterAdapter.addCharacters(apiResponse.getResults());

                        if (apiResponse.getInfo() != null && apiResponse.getInfo().getNext() != null) {
                            nextPageUrl = apiResponse.getInfo().getNext();
                        } else {
                            nextPageUrl = null;
                            buttonLoadMore.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "All characters loaded", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        handleApiError("Failed to fetch more data. Code: " + response.code());
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching characters from URL: ", e);
                mainThreadHandler.post(() -> {
                    showLoading(false);
                    isLoading = false;
                    handleApiError("Error: No Internet Connection");
                });
            }
        });
    }


    private void showLoading(boolean show) {
        if (show) {
            progressBarList.setVisibility(View.VISIBLE);
            recyclerViewCharacters.setVisibility(View.GONE);
        } else {
            progressBarList.setVisibility(View.GONE);
            recyclerViewCharacters.setVisibility(View.VISIBLE);
        }
    }

    private void handleApiError(String errorMessage) {
        // Sembunyikan semua item dan tampilkan tombol "Try Again"
        allCharacters.clear();
        characterAdapter.setCharacters(allCharacters);
        textViewError.setText(errorMessage);
        textViewError.setVisibility(View.VISIBLE);
        buttonTryAgain.setVisibility(View.VISIBLE);
        recyclerViewCharacters.setVisibility(View.GONE);
        buttonLoadMore.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(Character character) {
        // Navigasi ke DetailFragment pakai ID
        CharacterDetailFragment detailFragment = CharacterDetailFragment.newInstance(character.getId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Menghindari memory leak dengan membersihkan referensi view
        recyclerViewCharacters = null;
        progressBarList = null;
        textViewError = null;
        buttonTryAgain = null;
        buttonLoadMore = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}