package com.example.tuprak.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.PostDetailActivity;
import com.example.tuprak.ProfileActivity;
import com.example.tuprak.R;
import com.example.tuprak.models.UserPost;

import java.util.List;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.PostViewHolder> {
    
    private List<UserPost> postList;
    private Context context;

    public UserPostAdapter(Context context, List<UserPost> postList) {
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
        UserPost post = postList.get(position);
        
        holder.username.setText(post.getUsername());
        holder.caption.setText(post.getCaption());
        holder.likesCount.setText(String.format("%d likes", post.getLikesCount()));
        holder.timePosted.setText(post.getTimePosted());
        
        if (post.getUserProfileImageUri() != null && !post.getUserProfileImageUri().isEmpty()) {
            try {
                String profileUriStr = post.getUserProfileImageUri();
                if (profileUriStr.startsWith("android.resource://")) {
                    Uri profileUri = Uri.parse(profileUriStr);
                    String lastSegment = profileUri.getLastPathSegment();
                    
                    if (lastSegment != null) {
                        if (lastSegment.matches("\\d+")) {
                            int resId = Integer.parseInt(lastSegment);
                            holder.profileImage.setImageResource(resId);
                        } else {
                            String resourceName = lastSegment;
                            if (resourceName.contains("/")) {
                                resourceName = resourceName.substring(resourceName.lastIndexOf("/") + 1);
                            }
                            
                            int resId = holder.itemView.getContext().getResources().getIdentifier(
                                    resourceName, "drawable", holder.itemView.getContext().getPackageName());
                            
                            if (resId != 0) {
                                holder.profileImage.setImageResource(resId);
                            } else {
                                holder.profileImage.setImageResource(R.drawable.lab_logo);
                            }
                        }
                    } else {
                        holder.profileImage.setImageResource(R.drawable.lab_logo);
                    }
                } else {
                    holder.profileImage.setImageURI(Uri.parse(profileUriStr));
                }
            } catch (Exception e) {
                Log.e("UserPostAdapter", "Error loading profile image", e);
                holder.profileImage.setImageResource(R.drawable.lab_logo);
            }
        } else {
            holder.profileImage.setImageResource(R.drawable.lab_logo);
        }

        if (post.getPostImageUri() != null && !post.getPostImageUri().isEmpty()) {
            try {
                holder.postImage.setImageURI(Uri.parse(post.getPostImageUri()));
            } catch (Exception e) {
                holder.postImage.setImageResource(R.drawable.ic_bangrang);
            }
        } else {
            holder.postImage.setImageResource(R.drawable.ic_bangrang);
        }
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
            intent.putExtra("post_id", post.getId());
            holder.itemView.getContext().startActivity(intent);
        });
        holder.postImage.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
            intent.putExtra("post_id", post.getId());
            holder.itemView.getContext().startActivity(intent);
        });
        holder.likeButton.setOnClickListener(v -> {
            boolean isLiked = holder.likeButton.getTag() != null && (boolean) holder.likeButton.getTag();
            if (isLiked) {
                holder.likeButton.setImageResource(R.drawable.ic_like);
                holder.likeButton.setTag(false);
            } else {
                holder.likeButton.setImageResource(R.drawable.ic_like_filled);
                holder.likeButton.setTag(true);
            }
        });

        View.OnClickListener profileClickListener = view -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("username", post.getUsername());
            context.startActivity(intent);
        };
        
        holder.profileImage.setOnClickListener(profileClickListener);
        holder.username.setOnClickListener(profileClickListener);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    
    public void updatePosts(List<UserPost> newPosts) {
        this.postList = newPosts;
        notifyDataSetChanged();
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
            
            likeButton.setTag(false);
        }
    }
}