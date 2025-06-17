package com.example.tuprak.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuprak.ProfileActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.MainActivity;
import com.example.tuprak.PostDetailActivity;
import com.example.tuprak.R;
import com.example.tuprak.models.Post;
import com.example.tuprak.models.UserPost;
import com.example.tuprak.PostManager;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        
        Log.d("PostAdapter", "Binding post at position " + position + 
              " with profile image ID: " + post.getProfileImage() +
              " and post image ID: " + post.getPostImage());
        
        try {
            holder.profileImage.setImageResource(post.getProfileImage());
        } catch (Exception e) {
            Log.e("PostAdapter", "Error setting profile image", e);
            holder.profileImage.setImageResource(R.drawable.lab_logo);
        }
        
        try {
            holder.postImage.setImageResource(post.getPostImage());
        } catch (Exception e) {
            Log.e("PostAdapter", "Error setting post image", e);
            holder.postImage.setImageResource(R.drawable.ic_bangrang);
        }
        
        holder.username.setText(post.getUsername());
        holder.caption.setText(post.getCaption());
        holder.likesCount.setText(String.format("%d likes", post.getLikes()));
        holder.timePosted.setText(post.getTimePosted());
        
        if (post.getComments() > 0) {
            holder.commentsText.setText(String.format("View all %d comments", post.getComments()));
            holder.commentsText.setVisibility(View.VISIBLE);
        } else {
            holder.commentsText.setVisibility(View.GONE);
        }
        holder.likeButton.setTag(false);
        holder.likeButton.setOnClickListener(v -> {
            boolean isLiked = (boolean) holder.likeButton.getTag();
            if (isLiked) {
                holder.likeButton.setImageResource(R.drawable.ic_like);
                holder.likeButton.setTag(false);
            } else {
                holder.likeButton.setImageResource(R.drawable.ic_like_filled);
                holder.likeButton.setTag(true);
            }
        });
        
        View.OnClickListener profileClickListener = v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("username", post.getUsername());
            intent.putExtra("from_feed", true);
            context.startActivity(intent);
        };

        holder.profileImage.setOnClickListener(profileClickListener);
        holder.username.setOnClickListener(profileClickListener);
        holder.postImage.setOnClickListener(v -> {
            PostManager postManager = new PostManager(context);
            List<UserPost> userPosts = postManager.getPostsByUsername(post.getUsername());
            if (!userPosts.isEmpty()) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("post_id", userPosts.get(position % userPosts.size()).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage, postImage, likeButton, commentButton, shareButton, saveButton;
        TextView username, caption, likesCount, commentsText, timePosted;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.postProfileImage);
            username = itemView.findViewById(R.id.postUsername);
            postImage = itemView.findViewById(R.id.postImage);
            caption = itemView.findViewById(R.id.postCaption);
            likesCount = itemView.findViewById(R.id.postLikesCount);
            commentsText = itemView.findViewById(R.id.postCommentsText);
            timePosted = itemView.findViewById(R.id.postTimeText);

            likeButton = itemView.findViewById(R.id.postLikeButton);
            commentButton = itemView.findViewById(R.id.postCommentButton);
            shareButton = itemView.findViewById(R.id.postShareButton);
            saveButton = itemView.findViewById(R.id.postSaveButton);
        }
    }

    private String formatCaption(String username, String caption) {
        return username + " " + caption;
    }
}