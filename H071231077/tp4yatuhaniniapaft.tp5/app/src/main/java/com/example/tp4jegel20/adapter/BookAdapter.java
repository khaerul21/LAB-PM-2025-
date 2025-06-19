// BookAdapter.java
package com.example.tp4jegel20.adapter;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tp4jegel20.R;
import com.example.tp4jegel20.model.Book;
import com.example.tp4jegel20.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private static final String TAG = "BookAdapter";
    private final List<Book> bookList;
    private final List<Book> bookListFull;
    private final OnBookActionListener listener;

    public interface OnBookActionListener {
        void onFavoriteClick(Book book, boolean isFavorite);
    }

    public BookAdapter(List<Book> bookList, OnBookActionListener listener) {
        Log.d(TAG, "Constructor called with " + bookList.size() + " books");
        this.bookList = new ArrayList<>();
        this.bookListFull = new ArrayList<>();
        this.listener = listener;
        setBooks(bookList);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        Log.d(TAG, "onBindViewHolder at position " + position +
                " for book: " + book.getTitle());
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void filter(String text) {
        Log.d(TAG, "Filtering books with query: '" + text + "'");
        bookList.clear();

        if (text == null || text.trim().isEmpty()) {
            bookList.addAll(bookListFull);
            Log.d(TAG, "Search text empty, showing all " + bookList.size() + " books");
        } else {
            String searchText = text.toLowerCase().trim();

            // Cek jika mencari berdasarkan genre (diawali dengan "genre:")
            boolean isGenreSearch = searchText.startsWith("genre:");
            if (isGenreSearch) {
                String genreQuery = searchText.substring(6).trim(); // Hapus "genre:" dari query
                for (Book book : bookListFull) {
                    if (book.getGenre().toLowerCase().contains(genreQuery)) {
                        bookList.add(book);
                    }
                }
                Log.d(TAG, "Found " + bookList.size() + " books matching genre: '" + genreQuery + "'");
            } else {
                // Search biasa (title, author, atau genre)
                for (Book book : bookListFull) {
                    if (matchesSearchCriteria(book, searchText)) {
                        bookList.add(book);
                    }
                }
                Log.d(TAG, "Found " + bookList.size() + " books matching: '" + text + "'");
            }
        }

        notifyDataSetChanged();
    }

    private boolean matchesSearchCriteria(Book book, String searchText) {
        return book.getTitle().toLowerCase().contains(searchText) ||
                book.getAuthor().toLowerCase().contains(searchText) ||
                book.getGenre().toLowerCase().contains(searchText);
    }

    public void setBooks(List<Book> books) {
        Log.d(TAG, "Setting new books: " + (books != null ? books.size() : 0) + " items");

        bookList.clear();
        bookListFull.clear();

        if (books != null) {
            List<Book> newList = new ArrayList<>(books);
            bookList.addAll(newList);
            bookListFull.addAll(newList);
            Log.d(TAG, "Successfully set " + newList.size() + " books");
        }

        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageBook;
        private final TextView textTitle;
        private final TextView textAuthor;
        private final TextView textYear;
        private final TextView textGenre; // Tambahkan ini
        private final ImageView imageFavorite;
        private boolean isProcessingClick = false;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R.id.imageBook);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            textYear = itemView.findViewById(R.id.textYear);
            textGenre = itemView.findViewById(R.id.textGenre); // Tambahkan ini
            imageFavorite = itemView.findViewById(R.id.imageFavorite);

            setupClickListeners(itemView);
        }

        private void setupClickListeners(View itemView) {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Book book = bookList.get(position);
                    Log.d(TAG, "Opening details for: " + book.getTitle());
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("book", book);
                    v.getContext().startActivity(intent);
                }
            });

            imageFavorite.setOnClickListener(v -> {
                if (!isProcessingClick) {
                    isProcessingClick = true;
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Book book = bookList.get(position);
                        Log.d(TAG, "Toggling favorite for: " + book.getTitle());
                        listener.onFavoriteClick(book, book.isFavorite());
                    }
                    new Handler().postDelayed(() -> isProcessingClick = false, 300);
                }
            });
        }

        void bind(Book book) {
            textTitle.setText(book.getTitle());
            textAuthor.setText(book.getAuthor());
            textYear.setText(String.valueOf(book.getYearPublished()));
            textGenre.setText(book.getGenre()); // Tambahkan ini

            loadBookCover(book.getCoverImageUrl());
            updateFavoriteIcon(book.isFavorite());
        }

        private void loadBookCover(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.book_placeholder)
                        .error(R.drawable.book_error)
                        .centerCrop()
                        .into(imageBook);
            } else {
                imageBook.setImageResource(R.drawable.book_placeholder);
            }
        }

        private void updateFavoriteIcon(boolean isFavorite) {
            imageFavorite.setImageResource(
                    isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border
            );
        }
    }
}