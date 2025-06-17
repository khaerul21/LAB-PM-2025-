package com.example.praktikum03.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum03.R;
import com.example.praktikum03.activites.PostDetailActivity;
import com.example.praktikum03.models.Post;
import com.example.praktikum03.utils.ImageUtils;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder>{
    private Context context;
    private List<Post> postList;

    public ProfilePostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);

        // Cek apakah post ini memiliki gambar yang diupload oleh user
        if (ImageUtils.hasUploadedImage(post.getId())) {
            // Gunakan gambar yang diupload
            Bitmap uploadedImage = ImageUtils.getUploadedImage(post.getId());
            holder.imageViewPost.setImageBitmap(uploadedImage);
        } else {
            // Gunakan gambar default dari resource
            holder.imageViewPost.setImageResource(post.getImageResourceId());
        }

        // Click listener to navigate to post detail
        holder.imageViewPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("POST_ID", post.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addPost(Post post) {
        postList.add(0, post);
        notifyItemInserted(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPost = itemView.findViewById(R.id.imageViewPosts);
        }
    }
}
