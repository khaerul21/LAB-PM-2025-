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
import com.example.mylibraryapp.data.BookViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private final BookViewModel bookViewModel;

    public BookAdapter(List<Book> bookList, BookViewModel bookViewModel) {
        this.bookList = new ArrayList<>(bookList);
        this.bookViewModel = bookViewModel;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvWriter.setText(book.getAuthor());
        holder.tvPublished.setText(String.valueOf(book.getYear()));
        holder.tvBlurb.setText(book.getDescription());

        holder.imgPhoto.setImageResource(R.drawable.cover_buku);

        if (book.getBookStatus()) {
            holder.imgLikeStatus.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            holder.imgLikeStatus.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        holder.imgLikeStatus.setOnClickListener(v -> {
            bookViewModel.toggleBookStatus(book);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("book", book);
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateBookList(List<Book> newBookList) {
        if (newBookList != null) {
            this.bookList.clear();
            this.bookList.addAll(newBookList);
            notifyDataSetChanged();
        }
    }


    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto, imgLikeStatus;
        TextView tvTitle, tvWriter, tvPublished, tvBlurb;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            imgLikeStatus = itemView.findViewById(R.id.img_like_status);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvWriter = itemView.findViewById(R.id.tv_item_writer);
            tvPublished = itemView.findViewById(R.id.tv_published);
            tvBlurb = itemView.findViewById(R.id.tv_item_blurb);
        }
    }
}

