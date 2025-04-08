package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.contentTextView.setText(post.getContent());
        holder.commentCountTextView.setText(String.valueOf(post.getCommentCount()));
        holder.retweetCountTextView.setText(String.valueOf(post.getRetweetCount()));
        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView commentCountTextView;
        TextView retweetCountTextView;
        TextView likeCountTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.post_content);
            commentCountTextView = itemView.findViewById(R.id.comment_count);
            retweetCountTextView = itemView.findViewById(R.id.retweet_count);
            likeCountTextView = itemView.findViewById(R.id.like_count);
        }
    }
}