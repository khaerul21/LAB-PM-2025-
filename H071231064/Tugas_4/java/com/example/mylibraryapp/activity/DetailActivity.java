package com.example.mylibraryapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mylibraryapp.R;
import com.example.mylibraryapp.data.Book;
import com.example.mylibraryapp.data.BookViewModel;
import com.example.mylibraryapp.data.BookViewModelFactory;

public class DetailActivity extends AppCompatActivity {

    private Book book;
    private ImageView btnFavorite;
    private BookViewModel bookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ambil Book dari Intent
        book = (Book) getIntent().getSerializableExtra("book");

        if (book == null) {
            Toast.makeText(this, "Data buku tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi ViewModel
        bookViewModel = new ViewModelProvider(
                this, // shared dengan fragment
                new BookViewModelFactory(getApplication())
        ).get(BookViewModel.class);

        // Hubungkan komponen UI
        ImageView imgBook = findViewById(R.id.img_book);
        TextView tvTitle = findViewById(R.id.tv_name_event);
        TextView tvAuthor = findViewById(R.id.tv_owner_event);
        TextView tvYear = findViewById(R.id.tv_begin_time);
        TextView tvDescription = findViewById(R.id.tv_item_description);
        btnFavorite = findViewById(R.id.btn_Favorite);

        // Tampilkan informasi buku
        imgBook.setImageResource(R.drawable.cover_buku); // image dummy
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvYear.setText("Tahun Terbit: " + book.getYear());
        tvDescription.setText(book.getDescription());

        updateFavoriteIcon(book.getBookStatus());

        // Toggle status favorite saat tombol diklik
        btnFavorite.setOnClickListener(view -> {
            // Toggle status favorit
            bookViewModel.toggleBookStatus(book); // Update status di ViewModel
            book.setBookStatus(!book.getBookStatus()); // update status lokal juga

            // Perbarui ikon
            updateFavoriteIcon(book.getBookStatus());

            Toast.makeText(
                    this,
                    book.getBookStatus() ? "Ditambahkan ke Favorit" : "Dihapus dari Favorit",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void updateFavoriteIcon(boolean isLiked) {
        btnFavorite.setImageResource(
                isLiked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24
        );
    }
}
