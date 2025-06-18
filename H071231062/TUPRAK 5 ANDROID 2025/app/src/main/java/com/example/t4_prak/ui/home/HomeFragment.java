package com.example.t4_prak.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t4_prak.BookAdapter;
import com.example.t4_prak.R;
import com.example.t4_prak.databinding.FragmentHomeBinding;
import com.example.t4_prak.Book;
import com.example.t4_prak.BookRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BookAdapter adapter;
    private List<Book> bookList;
    private ExecutorService executorService;
    private Handler mainHandler;
    private String selectedGenre = "All"; // Default untuk genre
    private String searchQuery = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        bookList = BookRepository.getInstance().getBooks();
        adapter = new BookAdapter(getContext(), bookList);


        RecyclerView recyclerView = binding.recyclerViewBooks;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // ini spinner untuk memilih genre
        setupGenreSpinner();


        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                filterBooks();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                filterBooks();
                return false;
            }
        });

        return root;
    }

    private void setupGenreSpinner() {
        Set<String> genres = new HashSet<>();
        genres.add("All");
        for (Book book : bookList) {
            genres.add(book.getGenre());
        }

        // Convert Set to List and set up Spinner adapter
        List<String> genreList = new ArrayList<>(genres);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, genreList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.genreSpinner.setAdapter(spinnerAdapter);


        binding.genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGenre = genreList.get(position);
                filterBooks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGenre = "All";
                filterBooks();
            }
        });
    }

    private void filterBooks() {

        // Tampilkan ProgressBar
        binding.progressBarHome.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            try {

                // Beri delay untuk simulasi
                Thread.sleep(2000);

                // Buat daftar buku yang difilter
                List<Book> filteredList = new ArrayList<>();

                for (Book book : bookList) {
                    boolean matchesGenre = selectedGenre.equals("All") || book.getGenre().equalsIgnoreCase(selectedGenre);
                    boolean matchesSearch = TextUtils.isEmpty(searchQuery) || book.getTitle().toLowerCase().contains(searchQuery.toLowerCase());

                    if (matchesGenre && matchesSearch) {
                        filteredList.add(book);
                    }
                }

                // Pastikan fragment masih aktif sebelum memperbarui UI
                if (isAdded() && binding != null) {
                    mainHandler.post(() -> {
                        // Perbarui RecyclerView di Main Thread
                        if (getContext() != null) {
                            adapter = new BookAdapter(getContext(), filteredList);
                            binding.recyclerViewBooks.setAdapter(adapter);
                        }

                        // Sembunyikan ProgressBar
                        binding.progressBarHome.setVisibility(View.GONE);
                    });
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Set binding ke null untuk mencegah kebocoran memori
        binding = null;

        // Hentikan ExecutorService
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}