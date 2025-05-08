package com.example.e_library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.e_library.DetailActivity;
import com.example.e_library.R;
import com.example.e_library.adapters.BookAdapter;
import com.example.e_library.models.Book;
import com.example.e_library.utils.BookData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private SearchView searchView;
    private ChipGroup chipGroupGenres;
    private String selectedGenre;
    private LottieAnimationView lottieAnimationView;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvBooks = view.findViewById(R.id.rv_books);
        searchView = view.findViewById(R.id.search_view);
        chipGroupGenres = view.findViewById(R.id.chipGroupGenres);
        lottieAnimationView = view.findViewById(R.id.lottie_loader);
        
        setupRecyclerView();
        setupSearchView();
        setupGenreChips();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks();
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        executor.execute(() -> {
            List<Book> allBooks = BookData.getAllBooks();
            mainHandler.postDelayed(() -> {
                bookAdapter = new BookAdapter(getContext(), allBooks, book -> {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_BOOK, book);
                    startActivity(intent);
                });
                rvBooks.setAdapter(bookAdapter);
                lottieAnimationView.cancelAnimation();
                lottieAnimationView.setVisibility(View.GONE);
            }, 1000);
        });
    }

    private void setupGenreChips() {
        List<String> genres = BookData.getAllGenres();
        chipGroupGenres.removeAllViews();
        for (String genre : genres) {
            Chip chip = new Chip(getContext());
            chip.setText(genre);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedGenre = genre;
                } else {
                    selectedGenre = "";
                }
                filterBooks();
            });
            chipGroupGenres.addView(chip);
        }
    }

    private void filterBooks() {
        rvBooks.setVisibility(View.GONE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        executor.execute(() -> {
            String query = searchView.getQuery().toString().toLowerCase();
            List<Book> filtered = new ArrayList<>();
            for (Book book : BookData.getAllBooks()) {
                boolean matchesGenre = selectedGenre == null || selectedGenre.isEmpty() || book.getGenre().equals(selectedGenre);
                boolean matchesQuery = book.getTitle().toLowerCase().contains(query);
                if (matchesGenre && matchesQuery) {
                    filtered.add(book);
                }
            }
            mainHandler.postDelayed(() -> {
                bookAdapter.updateData(filtered);
                lottieAnimationView.cancelAnimation();
                lottieAnimationView.setVisibility(View.GONE);
                rvBooks.setVisibility(View.VISIBLE);
            }, 1000);
        });
    }

    // Memperbarui daftar buku di Home
    @Override
    public void onResume() {
        super.onResume();
        if (bookAdapter != null) {
            List<Book> currentBooks = BookData.getAllBooks();
            bookAdapter.updateData(currentBooks);

            // Reset filter search view
            searchView.setQuery("", false);
            searchView.clearFocus();
        } else {
            setupRecyclerView();
        }
    }
}