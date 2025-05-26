package com.example.tuprak.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.R;
import com.example.tuprak.StoryViewerActivity;
import com.example.tuprak.models.Story;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private Context context;
    private List<Story> storyList;

    public StoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storyList.get(position);
        
        // Set username
        holder.username.setText(story.getUsername());
        
        // Check if story has custom profile image (for "Your Story")
        if (story.hasCustomProfileImage()) {
            // Use the URI instead of resource ID
            holder.profileImage.setImageURI(Uri.parse(story.getCustomProfileImageUri()));
        } else {
            // Use the resource ID as before
            holder.profileImage.setImageResource(story.getProfileImage());
        }
        
        // Rest of your existing binding code...
        if (story.isUserStory()) {
            holder.storyRing.setVisibility(View.GONE);
            holder.addBadge.setVisibility(View.VISIBLE);
        } else if (story.hasStory()) {
            holder.storyRing.setVisibility(View.VISIBLE);
            holder.addBadge.setVisibility(View.GONE);

            if (story.isViewed()) {
                holder.storyRing.setColorFilter(Color.parseColor("#777777"));
            } else {
                holder.storyRing.setColorFilter(null);
            }
        } else {
            holder.storyRing.setVisibility(View.GONE);
            holder.addBadge.setVisibility(View.GONE);
        }
        
        if (story.hasStory() || story.isUserStory()) {
            holder.itemView.setOnClickListener(v -> openStoryViewer(story, position));
        }
    }
    
    private void openStoryViewer(Story story, int position) {
        Intent intent = new Intent(context, StoryViewerActivity.class);
        intent.putExtra("title", story.getUsername());
        intent.putExtra("image_resource", story.getProfileImage());
        intent.putExtra("timestamp", "3 HOURS AGO");
        intent.putExtra("story_position", position);
        
        if (!story.isUserStory()) {
            story.setViewed(true);
            notifyItemChanged(position);
        }
        
        context.startActivity(intent);
        
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(R.anim.fade_in, 0);
        }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView username;
        ImageView storyRing;
        TextView addBadge;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.storyProfileImage);
            username = itemView.findViewById(R.id.storyUsername);
            storyRing = itemView.findViewById(R.id.storyRing);
            addBadge = itemView.findViewById(R.id.storyAddBadge);
        }
    }
}