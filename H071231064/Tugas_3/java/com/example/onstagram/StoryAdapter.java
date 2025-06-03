package com.example.onstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private Context context;
    private List<Story> stories;
    private List<User> userList;
    private List<Feed> feedList;

    public StoryAdapter(Context context, List<Story> stories, List<User> userList, List<Feed> feedList) {
        this.context = context;
        this.stories = stories;
        this.userList = userList;
        this.feedList = feedList;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.storyName.setText(story.getUsername());

        User user = findUserByUsername(story.getUsername());
        List<Feed> userFeeds = getFeedsByUsername(story.getUsername());
        if (user != null) {
            Glide.with(context)
                    .load(story.getImageUrl())
                    .circleCrop()
                    .into(holder.storyImage);

            holder.storyImage.setOnClickListener(v -> {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("user", user);
                intent.putParcelableArrayListExtra("userFeeds", new ArrayList<>(userFeeds));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView storyImage;
        TextView storyName;

        public StoryViewHolder(View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.storyImage);
            storyName = itemView.findViewById(R.id.storyName);
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

