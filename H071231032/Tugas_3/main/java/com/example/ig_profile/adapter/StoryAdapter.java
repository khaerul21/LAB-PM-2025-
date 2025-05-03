package com.example.ig_profile.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.ig_profile.activity.DetailHighlightActivity;
import com.example.ig_profile.models.StoryItem;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private final List<StoryItem> storyList;
    private final Context context;

    public StoryAdapter(List<StoryItem> storyList, Context context) {
        this.storyList = storyList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_highlight, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        StoryItem item = storyList.get(position);

        Glide.with(context)
                .load(item.getCover())
                .circleCrop()
                .into(holder.cover);

        if (item.getTitle().equals(DataHolder.getInstance().getCurrentUser().getUsername())) {
            holder.title.setText("Your story");
        } else {
            holder.title.setText(item.getTitle());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailHighlightActivity.class); // Bisa pakai DetailStoryActivity kalau punya
            intent.putExtra("title", item.getTitle());
            intent.putExtra("coverUri", item.getCover());

            int[] imageArray = new int[item.getImageList().size()];
            for (int i = 0; i < item.getImageList().size(); i++) {
                imageArray[i] = item.getImageList().get(i);
            }
            intent.putExtra("images", imageArray);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return storyList != null ? storyList.size() : 0;
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.highlightCover);
            title = itemView.findViewById(R.id.highlightTitle);
        }
    }
}