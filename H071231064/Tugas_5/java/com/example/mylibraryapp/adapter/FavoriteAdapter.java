package com.example.mylibraryapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibraryapp.R;
import com.example.mylibraryapp.activity.DetailActivity;
import com.example.mylibraryapp.data.Book;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Book> favoriteBooks;

    public FavoriteAdapter(List<Book> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_list_book untuk setiap item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        Book book = favoriteBooks.get(position);

        // Bind data ke tampilan
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvYear.setText(String.valueOf(book.getYear()));
        holder.tvDescription.setText("Stok: " + book.getStock());

        // Placeholder untuk gambar (gunakan gambar sesuai kebutuhan, misalnya cover buku)
        holder.ivCover.setImageResource(R.drawable.cover_buku);  // Default placeholder

        // Tampilkan status "favorite" (like atau tidak)
        if (book.getBookStatus()) {
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_24); // Gambar status favorite
        } else {
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_border_24); // Gambar status non-favorite
        }

        // Set listener untuk item click untuk menuju DetailActivity
        holder.itemView.setOnClickListener(v -> {
            // Pastikan Book mengimplementasikan Serializable atau Parcelable untuk mengirimkan data ke Activity lain
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("book", book); // Kirim data buku ke DetailActivity
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteBooks.size();  // Mengembalikan jumlah item dalam daftar buku favorit
    }

    public void updateBookList(List<Book> newFavoriteBooks) {
        this.favoriteBooks = newFavoriteBooks;
        notifyDataSetChanged();  // Memberitahu adapter untuk memperbarui tampilan
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvYear, tvDescription;
        ImageView ivCover, imgLike;

        public ViewHolder(View itemView) {
            super(itemView);

            // Inisialisasi view pada item list
            ivCover = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvAuthor = itemView.findViewById(R.id.tv_item_writer);
            tvYear = itemView.findViewById(R.id.tv_published);
            tvDescription = itemView.findViewById(R.id.tv_item_blurb);
            imgLike = itemView.findViewById(R.id.img_like_status);  // Gambar status favorite (like)
        }
    }
}
