package com.example.praktikum03.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum03.R;
import com.example.praktikum03.activites.PostDetailActivity;
import com.example.praktikum03.activites.ProfileActivity;
import com.example.praktikum03.models.Post;
import com.example.praktikum03.utils.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public FeedAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.imageViewProfile.setImageResource(post.getUser().getProfileImageResourceId());
        holder.textViewUsername.setText(post.getUser().getUsername());

        if (ImageUtils.hasUploadedImage(post.getId())) {
            Bitmap uploadedImage = ImageUtils.getUploadedImage(post.getId());
            holder.imageViewPost.setImageBitmap(uploadedImage);
        } else {
            holder.imageViewPost.setImageResource(post.getImageResourceId());
        }

        holder.textViewLikes.setText(String.valueOf(post.getLikesCount()));
        holder.textViewComments.setText(String.valueOf(post.getCommentsCount()));
        holder.textViewShares.setText(String.valueOf(post.getSharesCount()));
        holder.textViewCaption.setText(post.getCaption());

        holder.layoutPostHeader.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("USER_ID", post.getUser().getId());
            context.startActivity(intent);
        });

        holder.imageViewPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("POST_ID", post.getId());
            context.startActivity(intent);
        });
        Log.d("FeedAdapter", "Binding post: " + post.getCaption());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutPostHeader;
        CircleImageView imageViewProfile;
        ImageView imageViewPost;
        TextView textViewUsername, textViewLikes, textViewComments, textViewShares, textViewCaption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutPostHeader = itemView.findViewById(R.id.layoutPostHeader);
            imageViewProfile = itemView.findViewById(R.id.iv_profile);
            imageViewPost = itemView.findViewById(R.id.imageViewPosts);
            textViewUsername = itemView.findViewById(R.id.tv_username);
            textViewLikes = itemView.findViewById(R.id.tv_likes);
            textViewComments = itemView.findViewById(R.id.tv_comments);
            textViewShares = itemView.findViewById(R.id.tv_shares);
            textViewCaption = itemView.findViewById(R.id.tv_caption);

        }
    }
}