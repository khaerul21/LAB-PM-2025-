package com.example.t4_prak;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String title = getIntent().getStringExtra("BOOK_TITLE");
        String author = getIntent().getStringExtra("BOOK_AUTHOR");
        int year = getIntent().getIntExtra("BOOK_YEAR", 0);
        String blurb = getIntent().getStringExtra("BOOK_BLURB");
        String genre = getIntent().getStringExtra("BOOK_GENRE");
        float rating = getIntent().getFloatExtra("BOOK_RATING", 0);
        String coverUri = getIntent().getStringExtra("BOOK_COVER_URI");
        int cover = getIntent().getIntExtra("BOOK_COVER", 0);
        int coverResId = getIntent().getIntExtra("BOOK_COVER", 0);

        TextView titleTextView = findViewById(R.id.text_detail_title);
        TextView authorTextView = findViewById(R.id.text_detail_author);
        TextView yearTextView = findViewById(R.id.text_detail_year);
        TextView blurbTextView = findViewById(R.id.text_detail_blurb);
        TextView genreTextView = findViewById(R.id.text_detail_genre);
        TextView ratingTextView = findViewById(R.id.text_detail_rating);
        ImageView coverImageView = findViewById(R.id.image_detail_cover);
        Button likeButton = findViewById(R.id.button_like);

        titleTextView.setText(title != null ? title : "Unknown Title");
        authorTextView.setText(author != null ? author : "Unknown Author");
        yearTextView.setText(year > 0 ? String.valueOf(year) : "Unknown Year");
        blurbTextView.setText(blurb != null ? blurb : "No description available.");
        genreTextView.setText(genre != null ? "Genre: " + genre : "Genre: Unknown");
        ratingTextView.setText("Rating: " + rating);

        if (coverUri != null) {

            coverImageView.setImageURI(Uri.parse(coverUri));
        } else if (coverResId != 0) {

            coverImageView.setImageResource(coverResId);
        } else {

            coverImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }


        BookRepository repository = BookRepository.getInstance();
        Book book = repository.getBooks().stream()
                .filter(b -> b.getTitle().equals(title))
                .findFirst()
                .orElse(null);

        if (book != null) {
            likeButton.setText(book.isLiked() ? "Unlike" : "Like");

            likeButton.setOnClickListener(v -> {
                book.setLiked(!book.isLiked());
                likeButton.setText(book.isLiked() ? "Unlike" : "Like");
            });
        }
    }
}