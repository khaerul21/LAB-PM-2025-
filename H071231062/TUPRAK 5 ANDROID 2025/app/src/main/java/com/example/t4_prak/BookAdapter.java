package com.example.t4_prak;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final Context context;
    private final List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.genre.setText("Genre: " + book.getGenre());
        holder.rating.setText("Rating: " + book.getRating());

        // Load cover image from URI or fallback to resource ID
        if (book.getCoverImageUri() != null) {
            holder.cover.setImageURI(Uri.parse(book.getCoverImageUri()));
        } else {
            holder.cover.setImageResource(book.getCoverImageResId());
        }

        // OnClickListener to open DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("BOOK_TITLE", book.getTitle());
            intent.putExtra("BOOK_AUTHOR", book.getAuthor());
            intent.putExtra("BOOK_YEAR", book.getYear());
            intent.putExtra("BOOK_BLURB", book.getBlurb());
            intent.putExtra("BOOK_COVER", book.getCoverImageResId());
            intent.putExtra("BOOK_COVER_URI", book.getCoverImageUri());
            intent.putExtra("BOOK_GENRE", book.getGenre());
            intent.putExtra("BOOK_RATING", book.getRating());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author,genre,rating;
        ImageView cover;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_book_title);
            author = itemView.findViewById(R.id.text_book_author);
            genre = itemView.findViewById(R.id.text_book_genre);
            rating = itemView.findViewById(R.id.text_book_rating);
            cover = itemView.findViewById(R.id.image_book_cover);
        }
    }
}