// FavoritesFragment.java
package com.example.tp4jegel20.ui.favorites;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp4jegel20.R;
import com.example.tp4jegel20.adapter.BookAdapter;
import com.example.tp4jegel20.data.BookRepository;
import com.example.tp4jegel20.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment {
    private static final String TAG = "FavoritesFragment";
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private TextView emptyView;
    private ProgressBar progressBar;
    private Handler mainHandler;
    private ExecutorService executorService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called");

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        emptyView = view.findViewById(R.id.textEmpty);
        progressBar = view.findViewById(R.id.progressBar);

        mainHandler = new Handler(Looper.getMainLooper());
        executorService = Executors.newSingleThreadExecutor();

        setupRecyclerView();
        loadFavoriteBooksAsync();
    }

    private void setupRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        bookAdapter = new BookAdapter(new ArrayList<>(), new BookAdapter.OnBookActionListener() {
            @Override
            public void onFavoriteClick(Book book, boolean isFavorite) {
                Log.d(TAG, "Favorite clicked: " + book.getTitle() + " - changing to: " + !isFavorite);
                BookRepository.getInstance().toggleFavorite(book);

                mainHandler.postDelayed(() -> {
                    loadFavoriteBooksAsync();
                    Log.d(TAG, "Reloaded favorites after toggle");
                }, 100);
            }
        });

        recyclerView.setAdapter(bookAdapter);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                requireContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Log.d(TAG, "RecyclerView setup completed");
    }

    private void loadFavoriteBooksAsync() {
        Log.d(TAG, "loadFavoriteBooksAsync called");
        showLoading();

        executorService.execute(() -> {
            try {
                Thread.sleep(1000);

                List<Book> favorites = BookRepository.getInstance().getFavoriteBooks();
                Log.d(TAG, "Received favorite books: " + favorites.size() + " items");

                for (Book book : favorites) {
                    Log.d(TAG, String.format("Book: %s, Favorite: %b, Hash: %d",
                            book.getTitle(), book.isFavorite(), book.hashCode()));
                }

                mainHandler.post(() -> {
                    if (isAdded() && bookAdapter != null) {
                        bookAdapter.setBooks(favorites);
                        updateEmptyView(favorites.isEmpty());

                        Log.d(TAG, "UI updated. Favorites count: " + favorites.size());
                        Log.d(TAG, "RecyclerView visibility: " +
                                (recyclerView.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
                    } else {
                        Log.e(TAG, "Fragment not attached or adapter is null");
                    }
                    hideLoading();
                });
            } catch (InterruptedException e) {
                Log.e(TAG, "Loading operation interrupted", e);
                mainHandler.post(this::hideLoading);
            }
        });
    }

    private void updateEmptyView(boolean isEmpty) {
        Log.d(TAG, "updateEmptyView: isEmpty = " + isEmpty);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
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
        loadFavoriteBooksAsync();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}