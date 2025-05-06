package com.example.praktikum03.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum03.R;
import com.example.praktikum03.activites.StoryActivity;
import com.example.praktikum03.models.Story;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context context;
    private List<Story> storyList;

    public StoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = storyList.get(position);

        // Set story thumbnail and title
        holder.imageViewStory.setImageResource(story.getThumbnailResourceId());
        holder.textViewStoryTitle.setText(story.getTitle());

        // Click listener to navigate to story detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoryActivity.class);
            intent.putExtra("STORY_ID", story.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewStory;
        TextView textViewStoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewStory = itemView.findViewById(R.id.imageViewStory);
            textViewStoryTitle = itemView.findViewById(R.id.textViewStoryTitle);
        }
    }
}
