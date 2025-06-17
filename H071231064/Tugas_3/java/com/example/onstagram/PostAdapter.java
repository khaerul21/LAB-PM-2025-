package com.example.onstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Feed> feedList;

    public PostAdapter(Context context, List<Feed> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Feed feed = feedList.get(position);

        Glide.with(context)
                .load(feed.getFeedImageUrl())
                .centerCrop()
                .into(holder.imageFeed);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFeed;

        public PostViewHolder(View itemView) {
            super(itemView);
            imageFeed = itemView.findViewById(R.id.imageFeed);
        }
    }
}
