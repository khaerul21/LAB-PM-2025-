package com.example.mylibraryapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibraryapp.R;
import com.example.mylibraryapp.adapter.FavoriteAdapter;
import com.example.mylibraryapp.data.BookViewModel;
import com.example.mylibraryapp.data.BookViewModelFactory;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private BookViewModel bookViewModel;
    private FavoriteAdapter favoriteAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.rv_books);
        progressBar = view.findViewById(R.id.progressBar); // Menambahkan referensi ke ProgressBar
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteAdapter = new FavoriteAdapter(new ArrayList<>());
        recyclerView.setAdapter(favoriteAdapter);

        bookViewModel = new ViewModelProvider(
                requireActivity(),
                new BookViewModelFactory(requireActivity().getApplication())
        ).get(BookViewModel.class);

        // Menampilkan ProgressBar saat mengambil data
        showLoading(true);

        // Simulasikan delay 1 detik dan ambil data setelahnya
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Setelah delay 1 detik, ambil data dari ViewModel
                bookViewModel.getFavoriteBooks().observe(getViewLifecycleOwner(), books -> {
                    favoriteAdapter.updateBookList(books);
                    // Sembunyikan ProgressBar setelah data diterima
                    showLoading(false);
                });
            }
        }, 1000); // 1000ms = 1 detik

        return view;
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE); // Menampilkan ProgressBar
        } else {
            progressBar.setVisibility(View.GONE); // Menyembunyikan ProgressBar
        }
    }
}
