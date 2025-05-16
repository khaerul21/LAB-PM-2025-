package com.example.praktikum04.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.praktikum04.R;
import com.example.praktikum04.models.Book;
import com.example.utils.BookDummy;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivCover;
    private TextView tvTitle, tvAuthor, tvDesc, tvYear;
    private LinearLayout containerGenre;
    private Button btnFav;

    private Book book;
    private RatingBar ratingBarDetail;
    private TextView tvRatingNumberDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inisialisasi view
        ivCover = findViewById(R.id.imageCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDesc = findViewById(R.id.tvDescription);
        tvYear = findViewById(R.id.tvYear);
        btnFav = findViewById(R.id.btnFavorite);
        containerGenre = findViewById(R.id.containerGenre);
        ratingBarDetail = findViewById(R.id.ratingBarDetail);
        tvRatingNumberDetail = findViewById(R.id.tvRatingNumberDetail);

        // Ambil data book
        book = getIntent().getParcelableExtra("book");

        if (book != null) {
            showBookDetail(book);
        }

        // Toggle tombol Favorite
        btnFav.setOnClickListener(v -> {
            if (book != null) {
                boolean isFav = book.isFav();       // ambil status favorit sekarang
                book.setfav(!isFav);                // toggle status favorit

                if (!isFav) {
                    btnFav.setText("Hapus dari Favorit");
                    Toast.makeText(this, "Buku ditambahkan ke favorit!", Toast.LENGTH_SHORT).show();
                    if (!BookDummy.FavBooks.contains(book)) {
                        BookDummy.FavBooks.add(book);
                    }
                } else {
                    btnFav.setText("Tambah ke Favorit");
                    Toast.makeText(this, "Buku dihapus dari favorit!", Toast.LENGTH_SHORT).show();
                    BookDummy.FavBooks.remove(book);
                }
            }
    });
    }

    private void showBookDetail(Book book) {
        // Set rating di RatingBar
        ratingBarDetail.setRating(book.getRating());
        // Set angka rating di TextView
        tvRatingNumberDetail.setText(String.format("%.1f", book.getRating()));

        // Tampilkan gambar
        if (book.getImageUri() != null && !book.getImageUri().isEmpty()) {
            try {
                Uri uri = Uri.parse(book.getImageUri());

                // Use Glide with error handling
                Glide.with(this)
                        .load(uri)
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_background)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Glide", "Load image failed", e);
                                return false; // biar Glide tetap set error drawable
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                Log.d("Glide", "Image loaded successfully");
                                return false;
                            }
                        })
                        .into(ivCover);

                Log.d("DetailActivity", "Loading image from URI: " + uri);
            } catch (Exception e) {
                Log.e("DetailActivity", "Error loading image URI: " + e.getMessage());
                ivCover.setImageResource(R.drawable.ic_launcher_background);
            }
        } else if (book.getCoverResId() != -1) {
            ivCover.setImageResource(book.getCoverResId());
        } else {
            ivCover.setImageResource(R.drawable.ic_launcher_background);
        }

        // Tampilkan teks
        tvTitle.setText(book.getTitle());
        tvAuthor.setText("Penulis: " + book.getAuthor());
        tvDesc.setText(book.getDescription());
        tvYear.setText("Tahun Terbit: " + book.getYear());
        btnFav.setText(book.isFav() ? "Hapus dari Favorit" : "Tambah ke Favorit");

        // Genre (multi genre dengan chip style)
        containerGenre.removeAllViews();

        String[] genres = book.getGenre().split(",");
        for (String g : genres) {
            TextView genreChip = new TextView(this);
            genreChip.setText(g.trim());
            genreChip.setBackgroundResource(R.drawable.bg_pill_purple);
            genreChip.setTextColor(Color.WHITE);
            genreChip.setPadding(16, 8, 16, 8);
            genreChip.setTextSize(12);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            genreChip.setLayoutParams(params);

            containerGenre.addView(genreChip);
        }
    }

}
