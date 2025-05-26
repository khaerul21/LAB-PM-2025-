package com.example.tuprak.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.PostDetailActivity;
import com.example.tuprak.R;
import com.example.tuprak.models.UserPost;

import java.util.List;

public class ProfileGridAdapter extends RecyclerView.Adapter<ProfileGridAdapter.GridViewHolder> {
    
    private List<UserPost> postList;
    private Context context;

    public ProfileGridAdapter(Context context, List<UserPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_profile_grid, parent, false);
        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        UserPost post = postList.get(position);
        
        try {
            String uriStr = post.getPostImageUri();
            if (uriStr != null && !uriStr.isEmpty()) {
                if (uriStr.startsWith("android.resource://")) {
                    Uri uri = Uri.parse(uriStr);
                    String lastSegment = uri.getLastPathSegment();
                    
                    if (lastSegment != null) {
                        if (lastSegment.matches("\\d+")) {
                            int resId = Integer.parseInt(lastSegment);
                            holder.postImage.setImageResource(resId);
                        } else {
                            String resourceName = lastSegment;
                            if (resourceName.contains("/")) {
                                resourceName = resourceName.substring(resourceName.lastIndexOf("/") + 1);
                            }
                            
                            int resId = holder.itemView.getContext().getResources().getIdentifier(
                                    resourceName, "drawable", holder.itemView.getContext().getPackageName());
                            
                            if (resId != 0) {
                                holder.postImage.setImageResource(resId);
                            } else {
                                holder.postImage.setImageResource(R.drawable.ic_bangrang);
                            }
                        }
                    }
                } else {
                    holder.postImage.setImageURI(Uri.parse(uriStr));
                }
            } else {
                holder.postImage.setImageResource(R.drawable.ic_bangrang);
            }
        } catch (Exception e) {
            Log.e("ProfileGridAdapter", "Error loading image: " + post.getPostImageUri(), e);
            holder.postImage.setImageResource(R.drawable.ic_bangrang);
        }
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
            intent.putExtra("post_id", post.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    
    public void updatePosts(List<UserPost> newPosts) {
        this.postList = newPosts;
        notifyDataSetChanged();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.gridPostImage);
        }
    }
}