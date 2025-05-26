package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> { //1

    private List<Post> postList; //1

    public PostAdapter(List<Post> postList) { //1
        this.postList = postList; //1
    }

    @NonNull //2
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    } //2

    @Override //3
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.contentTextView.setText(post.getContent());
        holder.likesTextView.setText("Likes: " + post.getLikes());
        holder.commentsTextView.setText("Comments: " + post.getComments());
        holder.sharesTextView.setText("Shares: " + post.getShares());
    }

    @Override //4
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder { //5
        TextView contentTextView, likesTextView, commentsTextView, sharesTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.post_content);
            likesTextView = itemView.findViewById(R.id.post_likes);
            commentsTextView = itemView.findViewById(R.id.post_comments);
            sharesTextView = itemView.findViewById(R.id.post_shares);
        }
    }
}
