package com.example.e_library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment {

    private RecyclerView rvFavoriteBooks;
    private BookAdapter favoriteBookAdapter;
    private TextView tvNoFavorites;
    private List<Book> favoriteBooks;
    private LottieAnimationView lottieAnimationView;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavoriteBooks = view.findViewById(R.id.rv_favorite_books);
        tvNoFavorites = view.findViewById(R.id.tv_no_favorites);
        lottieAnimationView = view.findViewById(R.id.lottie_loader);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        if (rvFavoriteBooks.getAdapter() != null) {
            rvFavoriteBooks.setVisibility(View.GONE);
        }

        executor.execute(() -> {
            favoriteBooks = BookData.getFavoriteBooks();
            mainHandler.postDelayed(() -> {
                // Tampilkan pesan jika tidak ada favorit
                if (favoriteBooks.isEmpty()) {
                    tvNoFavorites.setVisibility(View.VISIBLE);
                    rvFavoriteBooks.setVisibility(View.GONE);
                } else {
                    tvNoFavorites.setVisibility(View.GONE);
                    rvFavoriteBooks.setVisibility(View.VISIBLE);
                }
                favoriteBookAdapter = new BookAdapter(getContext(), favoriteBooks, book -> {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_BOOK, book);
                    startActivity(intent);
                });
                rvFavoriteBooks.setAdapter(favoriteBookAdapter);
                lottieAnimationView.cancelAnimation();
                lottieAnimationView.setVisibility(View.GONE);
            }, 1000);
        });
    }

    // Perbarui daftar favorit setiap kali fragment ditampilkan
    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
    }
}