package com.example.mylibraryapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibraryapp.R;
import com.example.mylibraryapp.adapter.BookAdapter;
import com.example.mylibraryapp.data.Book;
import com.example.mylibraryapp.data.BookData;
import com.example.mylibraryapp.data.BookViewModel;
import com.example.mylibraryapp.data.BookViewModelFactory;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private BookViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rv_books);

        setupRecyclerView();
        observeBooks();

        viewModel.getFilteredBooks().observe(getViewLifecycleOwner(), filtered -> {
            bookAdapter.updateBookList(filtered);
        });

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        viewModel = new ViewModelProvider(
                requireActivity(),
                new BookViewModelFactory(requireActivity().getApplication())
        ).get(BookViewModel.class);

        // Inisialisasi adapter kosong dulu, nanti di-update di observer
        bookAdapter = new BookAdapter(new java.util.ArrayList<>(), viewModel);
        recyclerView.setAdapter(bookAdapter);
    }

    private void observeBooks() {
        viewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            // Update data apapun isinya (kosong pun tetap di-update)
            bookAdapter.updateBookList(books);
        });
    }

    public void onResume() {
        super.onResume();
        // Pastikan HomeFragment selalu ambil data lengkap, bukan yang favorite
        List<Book> allBooks = BookData.getBooks();
        bookAdapter.updateBookList(allBooks);
    }



}
