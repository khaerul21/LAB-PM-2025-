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
import com.example.ig_profile.DataHolder;
import com.example.ig_profile.R;
import com.example.ig_profile.activity.ProfileActivity;
import com.example.ig_profile.models.FeedItem;
import com.example.ig_profile.models.User;

import java.util.List;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.ViewHolder> {

    private final List<FeedItem> feedItems;
    private final Context context;
    private final List<User> userList; // untuk Home

    public HomeFeedAdapter(List<FeedItem> feedItems, List<User> userList, Context context) {
        this.feedItems = feedItems;
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.item_detail_feed;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFeedAdapter.ViewHolder holder, int position) {
        FeedItem item = feedItems.get(position);

        // Load image feed
        if (item.isUri() && item.getImageUri() != null) {
            Glide.with(context).load(item.getImageUri()).into(holder.imageView);
        } else if (!item.isUri() && item.getImageResId() != 0) {
            Glide.with(context).load(item.getImageResId()).into(holder.imageView);
        } else {
            // Bisa kasih placeholder kalo image error
            holder.imageView.setImageResource(R.drawable.placeholder_feed);
        }

        // Temukan user yang cocok untuk feed ini
        final User user = getUserForFeed(item);
        if (user == null) return;

        // Load profile image
        Glide.with(context)
                .load(Uri.parse(user.getProfileImageUriString()))
                .circleCrop()
                .into(holder.profileImage);

        // Set detail feed
        holder.caption.setText(item.getCaption());
        holder.likes.setText(String.valueOf(item.getLikes()));
        holder.comments.setText(String.valueOf(item.getComments()));
        holder.shares.setText(String.valueOf(item.getShares()));
        holder.username.setText(user.getUsername());

        // Handle profile image & username click
        View.OnClickListener profileClickListener = v -> openUserProfile(user);
        holder.profileImage.setOnClickListener(profileClickListener);
        holder.username.setOnClickListener(profileClickListener);
    }

    @Override
    public int getItemCount() {
        return feedItems != null ? feedItems.size() : 0;
    }

    // Helper: cari user yang punya FeedItem ini
    private User getUserForFeed(FeedItem item) {
        for (User user : userList) {
            if (user.getUsername().equals(item.getOwnerFeeds())) {
                return user;
            }
        }
        return null;
    }

    // Buka profil user
    private void openUserProfile(User user) {
        User currentUser = DataHolder.getInstance().getCurrentUser();

        if (currentUser != null && user.getUsername().equals(currentUser.getUsername())) {
            // Buka langsung ProfileActivity tanpa extra "user"
            context.startActivity(new Intent(context, ProfileActivity.class));
        } else {
            // Buka profil user lain
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("user", user);
            context.startActivity(intent);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, profileImage;
        TextView caption, likes, comments, shares, username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feedImage);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            caption = itemView.findViewById(R.id.caption);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            shares = itemView.findViewById(R.id.shares);
        }
    }
}