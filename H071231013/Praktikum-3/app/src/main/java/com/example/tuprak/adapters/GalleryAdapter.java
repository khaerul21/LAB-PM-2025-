package com.example.tuprak.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private List<Uri> imageUris;
    private Context context;
    private OnImageSelectedListener listener;
    private int selectedPosition = -1;

    public interface OnImageSelectedListener {
        void onImageSelected(Uri imageUri);
    }

    public GalleryAdapter(Context context, List<Uri> imageUris, OnImageSelectedListener listener) {
        this.context = context;
        this.imageUris = imageUris;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery_image, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        holder.imageView.setImageURI(imageUri);
        
        if (position == selectedPosition) {
            holder.selectionOverlay.setVisibility(View.VISIBLE);
        } else {
            holder.selectionOverlay.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            
            if (oldSelectedPosition != -1) {
                notifyItemChanged(oldSelectedPosition);
            }
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onImageSelected(imageUri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public void updateImages(List<Uri> newImageUris) {
        this.imageUris = newImageUris;
        notifyDataSetChanged();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View selectionOverlay;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageView);
            selectionOverlay = itemView.findViewById(R.id.selectionOverlay);
        }
    }
}