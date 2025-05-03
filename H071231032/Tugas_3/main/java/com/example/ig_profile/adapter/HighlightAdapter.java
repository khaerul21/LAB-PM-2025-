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
import com.example.ig_profile.R;
import com.example.ig_profile.activity.DetailHighlightActivity;
import com.example.ig_profile.models.HighlightItem;

import java.util.List;

public class HighlightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ADD = 0;
    private static final int TYPE_HIGHLIGHT = 1;

    private final List<HighlightItem> highlightList;
    private final Context context;
    private final boolean showAddButton;

    public HighlightAdapter(List<HighlightItem> highlightList, Context context, boolean showAddButton) {
        this.highlightList = highlightList;
        this.context = context;
        this.showAddButton = showAddButton;
    }

    @Override
    public int getItemViewType(int position) {
        return (showAddButton && position == 0) ? TYPE_ADD : TYPE_HIGHLIGHT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add_highlight, parent, false);
            return new AddViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_highlight, parent, false);
            return new HighlightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HIGHLIGHT) {
            int index = showAddButton ? position - 1 : position;
            HighlightItem item = highlightList.get(index);
            HighlightViewHolder hvh = (HighlightViewHolder) holder;

            Glide.with(context).load(item.getCoverResId()).circleCrop().into(hvh.cover);
            hvh.title.setText(item.getTitle());

            hvh.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailHighlightActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("cover", item.getCoverResId());

                int[] imageArray = new int[item.getImageList().size()];
                for (int i = 0; i < item.getImageList().size(); i++) {
                    imageArray[i] = item.getImageList().get(i);
                }
                intent.putExtra("images", imageArray);

                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (highlightList == null) return 0;

        return highlightList.size() + (showAddButton ? 1 : 0);
    }

    static class AddViewHolder extends RecyclerView.ViewHolder {
        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class HighlightViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;

        public HighlightViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.highlightCover);
            title = itemView.findViewById(R.id.highlightTitle);
        }
    }
}