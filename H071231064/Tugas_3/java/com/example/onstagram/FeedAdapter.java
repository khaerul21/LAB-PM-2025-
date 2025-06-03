package com.example.onstagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private Context context;
    private List<Feed> feedList;
    private List<User> userList;

    public FeedAdapter(Context context, List<Feed> feedList, List<User> userList) {
        this.context = context;
        this.feedList = feedList;
        this.userList = userList;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Feed feed = feedList.get(position);

        holder.username.setText(feed.getUsername());
        holder.feedCaption.setText(feed.getFeedCaption());

        User user = findUserByUsername(feed.getUsername());
        List<Feed> userFeeds = getFeedsByUsername(feed.getUsername());
        if (user != null) {
            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .circleCrop()
                    .into(holder.imageProfile);


            Glide.with(context)
                    .load(feed.getFeedImageUrl())
                    .into(holder.imageFeed);

            holder.userProfile.setOnClickListener(v -> {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("user", user);
                intent.putParcelableArrayListExtra("userFeeds", new ArrayList<>(userFeeds));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView username, feedCaption;
        ImageView imageProfile, imageFeed;
        RelativeLayout userProfile;

        public FeedViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            imageFeed = itemView.findViewById(R.id.imageFeed);
            feedCaption = itemView.findViewById(R.id.feedCaption);
            userProfile = itemView.findViewById(R.id.userProfile);
        }
    }

    private User findUserByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private List<Feed> getFeedsByUsername(String username) {
        List<Feed> userFeeds = new ArrayList<>();
        for (Feed feed : feedList) {
            if (feed.getUsername().equals(username)) {
                userFeeds.add(feed);
            }
        }
        return userFeeds;
    }

}

