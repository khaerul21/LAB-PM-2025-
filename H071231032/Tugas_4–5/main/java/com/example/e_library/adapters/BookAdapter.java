package com.example.e_library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_library.R;
import com.example.e_library.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements Filterable {

    private final Context context;
    private List<Book> bookList;
    private List<Book> bookListFull; // Untuk filtering
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context, List<Book> bookList, OnItemClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.bookListFull = new ArrayList<>(bookList); // Salin untuk filter
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
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // Untuk update data di adapter (misal setelah search atau dari fragment lain)
    public void updateData(List<Book> newBookList) {
        this.bookList.clear();
        this.bookList.addAll(newBookList);
        this.bookListFull = new ArrayList<>(newBookList); // Update list full
        notifyDataSetChanged();
    }


    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvYear, tvGenre;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_book_cover);
            tvTitle = itemView.findViewById(R.id.tv_book_title);
            tvAuthor = itemView.findViewById(R.id.tv_book_author);
            tvYear = itemView.findViewById(R.id.tv_book_year);
            tvGenre = itemView.findViewById(R.id.tv_book_genre);
        }

        public void bind(final Book book, final OnItemClickListener listener) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor());
            tvYear.setText(String.format(Locale.getDefault(), "Tahun: %d", book.getYear()));
            tvGenre.setText(book.getGenre());
            tvGenre.setVisibility(View.VISIBLE);

            // Memuat gambar cover
            if (book.getCoverImageUri() != null) {
                Glide.with(itemView.getContext())
                        .load(book.getCoverImageUri())
                        .placeholder(R.drawable.placeholder_cover) // Placeholder saat loading
                        .error(R.drawable.placeholder_cover) // Placeholder jika error
                        .into(ivCover);
            } else if (book.getCoverImageResId() != 0) {
                // Muat dari drawable resource
                Glide.with(itemView.getContext()).load(book.getCoverImageResId()).into(ivCover);
            } else {
                // Jika tidak ada keduanya, tampilkan placeholder
                ivCover.setImageResource(R.drawable.placeholder_cover);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(book));
        }
    }

    // --- Implementasi Filterable untuk SearchView ---
    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private final Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                // Jika tidak ada query, tampilkan semua
                filteredList.addAll(bookListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book book : bookListFull) {
                    // Filter berdasarkan judul
                    if (book.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bookList.clear();
            if (results.values instanceof List) {
                try {
                    List<?> resultList = (List<?>) results.values;
                    for (Object obj : resultList) {
                        if (obj instanceof Book) {
                            bookList.add((Book) obj);
                        }
                    }
                } catch (ClassCastException e) {
                    e.getStackTrace();
                }
            }
            notifyDataSetChanged(); // Update RecyclerView
        }
    };

}