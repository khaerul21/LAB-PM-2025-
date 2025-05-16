package com.example.praktikum04.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.praktikum04.R;
import com.example.praktikum04.activity.DetailActivity;
import com.example.praktikum04.adapters.BookAdapter;
import com.example.utils.BookDummy;


public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private ProgressBar progressBar;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_favorites);
        progressBar = view.findViewById(R.id.progress_bar_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAdapter(getContext(), BookDummy.FavBooks, book -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.VISIBLE); // Tampilkan loading

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {} // Simulasi delay

            if (isAdded()) {  // pastikan fragment masih terpasang
                requireActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged(); // refresh tampilan
                    progressBar.setVisibility(View.GONE); // Sembunyikan loading
                });
            }
        }).start();
    }

}
