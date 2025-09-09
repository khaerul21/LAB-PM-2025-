package com.example.tp4jegel20.ui.detail;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.tp4jegel20.R;
import com.example.tp4jegel20.data.BookRepository;
import com.example.tp4jegel20.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private Book book;
    private FloatingActionButton fabFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        book = getIntent().getParcelableExtra("book");
        if (book == null) {
            finish();
            return;
        }

        setupViews();
        loadBookData();
    }

    private void setupViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        fabFavorite = findViewById(R.id.fabFavorite);
        fabFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void loadBookData() {
        ImageView imageBook = findViewById(R.id.imageBook);
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textAuthor = findViewById(R.id.textAuthor);
        TextView textYear = findViewById(R.id.textYear);
        TextView textGenre = findViewById(R.id.textGenre); // Tambahkan ini
        TextView textBlurb = findViewById(R.id.textBlurb);

        Glide.with(this)
                .load(book.getCoverImageUrl())
                .placeholder(R.drawable.book_placeholder)
                .error(R.drawable.book_error)
                .into(imageBook);

        textTitle.setText(book.getTitle());
        textAuthor.setText(getString(R.string.author_format, book.getAuthor())); // "By %s"
        textYear.setText(getString(R.string.year_format, book.getYearPublished())); // "Published in %d"
        textGenre.setText(getString(R.string.genre_format, book.getGenre())); // "Genre: %s"
        textBlurb.setText(book.getBlurb());

        updateFavoriteIcon();
    }

    private void toggleFavorite() {
        book.setFavorite(!book.isFavorite());
        BookRepository.getInstance().updateBook(book);
        updateFavoriteIcon();
        saveFavoriteStatus();
    }

    private void updateFavoriteIcon() {
        fabFavorite.setImageResource(
                book.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border
        );
    }

    private void saveFavoriteStatus() {
        SharedPreferences prefs = getSharedPreferences("books", MODE_PRIVATE);
        prefs.edit().putBoolean("favorite_" + book.getTitle(), book.isFavorite()).apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}