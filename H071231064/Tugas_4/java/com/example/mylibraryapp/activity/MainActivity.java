package com.example.mylibraryapp.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibraryapp.R;
import com.example.mylibraryapp.adapter.BookAdapter;
import com.example.mylibraryapp.data.Book;
import com.example.mylibraryapp.data.BookViewModel;
import com.example.mylibraryapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SearchBar searchBar;
    private SearchView searchView;
    private BookViewModel mainViewModel;

    private BookViewModel bookViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Menggunakan ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Menginisialisasi SearchBar dan SearchView
        searchBar = binding.searchBar;
        searchView = binding.searchView;

        // Mengubah warna ActionBar menjadi biru (warna primary)
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightblue)));

        // Menetapkan judul ActionBar
        getSupportActionBar().setTitle("My Library App");

        // Menginisialisasi ViewModel
        mainViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            String keyword = searchView.getText().toString().trim();
            if (!keyword.isEmpty()) {
                bookViewModel.findBooksWithSearch(keyword);
            }
            searchView.hide();
            return true;
        });


        // Mengonfigurasi BottomNavigationView dengan NavController
        BottomNavigationView navView = binding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Mengonfigurasi AppBar dengan navigasi
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.favoriteFragment, R.id.addBookFragment)
                .build();
        NavigationUI.setupWithNavController(navView, navController);

        // Setup ActionBar
        getSupportActionBar().setTitle("My Library App"); // Set judul ActionBar

        // Setup SearchBar untuk berfungsi dengan SearchView
        setupSearchView();

        // Menangani perubahan fragment di BottomNavigationView
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.homeFragment) {
                // Menampilkan SearchBar saat berada di HomeFragment
                searchBar.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
            } else {
                // Menyembunyikan SearchBar di fragment lain
                searchBar.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
            }
        });
    }


    // Menangani pencarian pada SearchBar dan SearchView
    private void setupSearchView() {
        searchBar.setOnClickListener(v -> {
            searchView.show(); // buka kolom input pencarian
        });

        searchView.setupWithSearchBar(searchBar); // penting!
        searchView.bringToFront(); // tampilkan di atas fragment

        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            String query = searchView.getText().toString().trim();
            if (!query.isEmpty()) {
                mainViewModel.findBooksWithSearch(query);
                searchBar.setText(query); // tampilkan keyword di SearchBar
            }
            searchView.hide();
            return true;
        });
    }


    private void updateRecyclerViewWithBooks(List<Book> books) {
        // Mengupdate RecyclerView dengan daftar buku yang telah difilter berdasarkan pencarian
        // Misalnya dengan mengupdate adapter
        BookAdapter bookAdapter = new BookAdapter(new ArrayList<>(), bookViewModel);
        recyclerView.setAdapter(bookAdapter);

        BookAdapter adapter = (BookAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateBookList(books);
        }
    }


}
