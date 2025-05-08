package com.example.e_library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.e_library.models.Book;
import com.example.e_library.utils.BookData;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK = "extra_book";

    private ImageView ivCover;
    private TextView tvTitle, tvAuthor, tvYear, tvBlurb, tvGenre;
    private ToggleButton toggleLike;
    private RatingBar rbRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

         Toolbar toolbar = findViewById(R.id.toolbar_detail);
         setSupportActionBar(toolbar);

        // Aktifkan tombol back di ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Inisialisasi Views
        ivCover = findViewById(R.id.iv_detail_cover);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvAuthor = findViewById(R.id.tv_detail_author);
        tvYear = findViewById(R.id.tv_detail_year);
        tvBlurb = findViewById(R.id.tv_detail_blurb);
        toggleLike = findViewById(R.id.toggle_like);
        tvGenre = findViewById(R.id.tv_detail_genre);
        rbRating = findViewById(R.id.rb_detail_rating);

        // ambil data Book dari Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_BOOK)) {
            Book currentBook;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                currentBook = intent.getParcelableExtra(EXTRA_BOOK, Book.class);
            } else {
                // Versi lama
                currentBook = intent.getParcelableExtra(EXTRA_BOOK);
            }

            if (currentBook != null) {
                populateView(currentBook);
                setupLikeButton(currentBook);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(currentBook.getTitle());
                }
            } else {
                // Handle jika data buku tidak ditemukan
                Toast.makeText(this, "Gagal memuat detail buku.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Handle jika intent tidak membawa data EXTRA_BOOK
            Toast.makeText(this, "Data buku tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void populateView(Book book) {
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvYear.setText(String.format(Locale.getDefault(), "Tahun: %d", book.getYear()));
        tvBlurb.setText(book.getBlurb());

        // Tampilkan Genre
        if (book.getGenre() != null && !book.getGenre().isEmpty()) {
            tvGenre.setText(book.getGenre());
            tvGenre.setVisibility(TextView.VISIBLE);
        } else {
            tvGenre.setVisibility(TextView.GONE);
        }

        rbRating.setRating(book.getRating());

        // Muat gambar cover
        if (book.getCoverImageUri() != null) {
            Glide.with(this)
                    .load(book.getCoverImageUri())
                    .placeholder(R.drawable.placeholder_cover)
                    .error(R.drawable.placeholder_cover)
                    .into(ivCover);
        } else if (book.getCoverImageResId() != 0) {
            Glide.with(this)
                    .load(book.getCoverImageResId())
                    .placeholder(R.drawable.placeholder_cover)
                    .error(R.drawable.placeholder_cover)
                    .into(ivCover);
        } else {
            ivCover.setImageResource(R.drawable.placeholder_cover);
        }
    }

    private void setupLikeButton(Book book) {
        toggleLike.setChecked(book.isLiked());

        toggleLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update status favorite di data source (BookData)
            BookData.updateLikeStatus(book.getId(), isChecked);
            String message = isChecked ? "Ditambahkan ke favorit" : "Dihapus dari favorit";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}