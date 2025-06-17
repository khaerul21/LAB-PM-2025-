package com.example.praktikum04.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.praktikum04.R;
import com.example.praktikum04.activity.DetailActivity;
import com.example.praktikum04.adapters.BookAdapter;
import com.example.praktikum04.adapters.GenreAdapter;
import com.example.praktikum04.models.Book;
import com.example.utils.BookDummy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private SearchView searchView;
    private RecyclerView rvGenre;
    private GenreAdapter genreAdapter;

    private List<Book> bookList = new ArrayList<>();
    private List<Book> filteredList = new ArrayList<>();

    private ProgressBar progressBar;
    private TextView tvEmptySearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.rvBuku);
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressBarHome);
        tvEmptySearch = view.findViewById(R.id.tvEmptySearch);
        rvGenre = view.findViewById(R.id.rvGenre);

        // Set warna teks di SearchView
        SearchView.SearchAutoComplete searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.GRAY);
        searchEditText.setTextSize(16);

        // SearchView agar langsung bisa diketik tanpa harus klik ikon dulu
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();

        // Ambil data buku
        bookList = BookDummy.bookList;
        filteredList = new ArrayList<>(bookList);

        // Setup RecyclerView buku
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter(getContext(), filteredList, book -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Setup RecyclerView genre horizontal
        rvGenre.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        genreAdapter = new GenreAdapter(getGenreDisplayList(), this::filterBooksByGenre);
        rvGenre.setAdapter(genreAdapter);

        setupSearchView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update genre dan buku terbaru setiap kembali ke fragment
        if (isAdded()) {
            genreAdapter = new GenreAdapter(getGenreDisplayList(), this::filterBooksByGenre);
            rvGenre.setAdapter(genreAdapter);

            filteredList.clear();
            filteredList.addAll(BookDummy.bookList);
            adapter.notifyDataSetChanged();

            tvEmptySearch.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Tidak perlu submit khusus
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });
    }

    private void filterBooks(String keyword) {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {} // Simulasi delay
            List<Book> tempList = new ArrayList<>();
            if (keyword.isEmpty()) {
                tempList.addAll(bookList);
            } else {
                for (Book book : bookList) {
                    if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        tempList.add(book);
                    }
                }
            }

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    filteredList.clear();
                    filteredList.addAll(tempList);
                    adapter.notifyDataSetChanged();

                    tvEmptySearch.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);

                    progressBar.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    private Map<String, Integer> getGenreCountMap() {
        Map<String, Integer> genreCount = new HashMap<>();
        for (Book book : bookList) {
            String[] genres = book.getGenre().split(",");
            for (String g : genres) {
                String genre = g.trim();
                genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
            }
        }
        return genreCount;
    }

    private List<String> getGenreDisplayList() {
        Map<String, Integer> map = getGenreCountMap();
        List<String> displayList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            displayList.add(entry.getKey() + " (" + entry.getValue() + ")");
        }
        return displayList;
    }

    private void filterBooksByGenre(String genre) {
        filteredList.clear();
        for (Book book : bookList) {
            String[] genres = book.getGenre().split(",");
            for (String g : genres) {
                if (g.trim().equalsIgnoreCase(genre)) {
                    filteredList.add(book);
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();

        tvEmptySearch.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
