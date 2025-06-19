// HomeFragment.java
package com.example.tp4jegel20.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp4jegel20.R;
import com.example.tp4jegel20.adapter.BookAdapter;
import com.example.tp4jegel20.data.BookRepository;
import com.example.tp4jegel20.model.Book;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private SearchView searchView;
    private ChipGroup chipGroupGenres;
    private ProgressBar progressBar;
    private Handler mainHandler;
    private ExecutorService executorService;

    private void setupRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookAdapter = new BookAdapter(new ArrayList<>(), new BookAdapter.OnBookActionListener() {
            @Override
            public void onFavoriteClick(Book book, boolean isFavorite) {
                Log.d(TAG, "Favorite clicked for book: " + book.getTitle());
                BookRepository.getInstance().toggleFavorite(book);
                loadBooksAsync();
            }
        });
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        chipGroupGenres = view.findViewById(R.id.chipGroupGenres);
        progressBar = view.findViewById(R.id.progressBar);

        // Initialize thread handling
        mainHandler = new Handler(Looper.getMainLooper());
        executorService = Executors.newSingleThreadExecutor();

        setupRecyclerView();
        setupSearchView();
        setupGenreChips();
        loadBooksAsync();
    }

    private void setupSearchView() {
        searchView.setQueryHint("Search by title, author, or use genre:Fantasy");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private static final long SEARCH_DELAY_MS = 300;
            private Runnable searchRunnable;

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooksAsync(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) {
                    mainHandler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> filterBooksAsync(newText);
                mainHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
                return true;
            }
        });
    }

    private void setupGenreChips() {
        // Get unique genres from books
        Set<String> uniqueGenres = new HashSet<>();
        for (Book book : BookRepository.getInstance().getAllBooks()) {
            uniqueGenres.add(book.getGenre());
        }

        // Create chips for each genre
        for (String genre : uniqueGenres) {
            Chip chip = new Chip(requireContext());
            chip.setText(genre);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    searchView.setQuery("genre:" + genre, true);
                } else if (searchView.getQuery().toString().equals("genre:" + genre)) {
                    searchView.setQuery("", true);
                }
            });
            chipGroupGenres.addView(chip);
        }
    }

    private void filterBooksAsync(String query) {
        Log.d(TAG, "Filtering books with query: " + query);
        showLoading();
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);

                if (bookAdapter != null) {
                    mainHandler.post(() -> {
                        bookAdapter.filter(query);
                        hideLoading();
                    });
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Filter operation interrupted", e);
                mainHandler.post(this::hideLoading);
            }
        });
    }

    private void loadBooksAsync() {
        try {
            Log.d(TAG, "Loading books from repository...");
            showLoading();
            executorService.execute(() -> {
                try {
                    Thread.sleep(2000);

                    List<Book> books = BookRepository.getInstance().getAllBooks();
                    mainHandler.post(() -> {
                        if (books != null) {
                            Log.d(TAG, "Successfully loaded " + books.size() + " books");
                            if (bookAdapter != null) {
                                bookAdapter.setBooks(books);
                                Log.d(TAG, "Books set to adapter");
                            } else {
                                Log.e(TAG, "BookAdapter is null");
                            }
                        } else {
                            Log.e(TAG, "Retrieved null book list from repository");
                        }
                        hideLoading();
                    });
                } catch (InterruptedException e) {
                    Log.e(TAG, "Loading operation interrupted", e);
                    mainHandler.post(this::hideLoading);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error loading books: " + e.getMessage());
            e.printStackTrace();
            hideLoading();
        }
    }

    private void showLoading() {
        if (progressBar != null && progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
            progressBar.startAnimation(fadeIn);
        }
    }

    private void hideLoading() {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            Animation fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            progressBar.startAnimation(fadeOut);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        loadBooksAsync();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}