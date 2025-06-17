package com.example.t4_prak.ui.favorites;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t4_prak.BookAdapter;
import com.example.t4_prak.BookRepository;
import com.example.t4_prak.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private ExecutorService executorService;
    private Handler mainHandler;
    private View progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        // inisiasi recyclerView dan progressBar
        recyclerView = root.findViewById(R.id.recycler_view_favorites);
        progressBar = root.findViewById(R.id.progress_bar_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // inisiasi ExecutorService dan Handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());


        loadFavorites();

        return root;
    }

    private void loadFavorites() {

        progressBar.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            try {

                Thread.sleep(2000);


                var likedBooks = BookRepository.getInstance().getLikedBooks();


                mainHandler.post(() -> {
                    adapter = new BookAdapter(getContext(), likedBooks);
                    recyclerView.setAdapter(adapter);


                    progressBar.setVisibility(View.GONE);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executorService.shutdown();
    }
}