package com.example.ig_profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ig_profile.R;
import com.example.ig_profile.activity.DetailFeedActivity;
import com.example.ig_profile.models.FeedItem;
import com.example.ig_profile.models.User;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final List<FeedItem> feedItems;
    private final Context context;
    private final boolean isGridMode;
    private final User user;

    public FeedAdapter(List<FeedItem> feedItems, User user, Context context, boolean isGridMode) {
        this.user = user;
        this.feedItems = feedItems;
        this.context = context;
        this.isGridMode = isGridMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isGridMode ? R.layout.item_feed_grid : R.layout.item_detail_feed;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        FeedItem item = feedItems.get(position);

        // Load image feed
        if (item.isUri()) {
            Glide.with(context).load(item.getImageUri()).into(holder.imageView);
        } else {
            Glide.with(context).load(item.getImageResId()).into(holder.imageView);
        }

        // Kalau bukan grid, tampilkan detail
        if (!isGridMode && holder.profileImage != null) {
            Glide.with(context)
                    .load(Uri.parse(user.getProfileImageUriString()))
                    .circleCrop()
                    .into(holder.profileImage);

            holder.caption.setText(item.getCaption());
            holder.likes.setText(String.valueOf(item.getLikes()));
            holder.comments.setText(String.valueOf(item.getComments()));
            holder.shares.setText(String.valueOf(item.getShares()));
            holder.username.setText(user.getUsername());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailFeedActivity.class);
            intent.putExtra("feedPosition", position);
            intent.putExtra("user", user);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return feedItems != null ? feedItems.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, profileImage;
        TextView caption, likes, comments, shares, username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feedImage);
            try {
                profileImage = itemView.findViewById(R.id.profileImage);
                username = itemView.findViewById(R.id.username);
                caption = itemView.findViewById(R.id.caption);
                likes = itemView.findViewById(R.id.likes);
                comments = itemView.findViewById(R.id.comments);
                shares = itemView.findViewById(R.id.shares);
            } catch (Exception ignored) {}
        }
    }
}