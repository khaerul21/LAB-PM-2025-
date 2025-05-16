package com.example.praktikum04.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum04.R;
import com.example.praktikum04.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private OnItemClickListener listener;

    // Interface untuk menangani klik item
    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context, List<Book> bookList, OnItemClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.genreTextView.setText(book.getGenre());
        holder.blurbTextView.setText(book.getBlurb());

        // set rating
        holder.ratingBar.setRating(book.getRating());
        holder.tvRatingNumber.setText(String.format("%.1f", book.getRating()));

        // Load image dari URI
        if (book.getImageUri() != null) {
            Uri uri = Uri.parse(book.getImageUri());
            holder.coverImageView.setImageURI(uri);
        } else if (book.getCoverResId() != -1) {
            holder.coverImageView.setImageResource(book.getCoverResId());
        } else {
            holder.coverImageView.setImageResource(R.drawable.ic_launcher_background); // fallback
        }


        // Tangani klik
        holder.itemView.setOnClickListener(v -> listener.onItemClick(book));

        // Array warna
        int[] colors = {
                ContextCompat.getColor(holder.itemView.getContext(), R.color.book_bg_1),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.book_bg_2),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.book_bg_3),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.book_bg_4),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.book_bg_5),
        };

        // Ambil warna sesuai posisi
        int color = colors[position % colors.length];
        holder.itemView.findViewById(R.id.infoContainer).setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // ViewHolder
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, genreTextView, blurbTextView;;
        ImageView coverImageView;
        RatingBar ratingBar;
        TextView tvRatingNumber;


        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            authorTextView = itemView.findViewById(R.id.textAuthor);
            genreTextView = itemView.findViewById(R.id.tvGenre);
            coverImageView = itemView.findViewById(R.id.imageCover);
            blurbTextView = itemView.findViewById(R.id.tvBlurb);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvRatingNumber = itemView.findViewById(R.id.tvRatingNumber);
        }
    }
}


