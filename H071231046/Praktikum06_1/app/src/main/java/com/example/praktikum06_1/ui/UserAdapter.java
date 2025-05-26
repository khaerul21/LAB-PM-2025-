package com.example.praktikum06_1.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.praktikum06_1.R;
import com.example.praktikum06_1.response.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_LOAD_MORE = 2;

    private List<User> userList;
    private OnLoadMoreClickListener loadMoreClickListener;
    private boolean showLoadMore = false;
    private OnUserClickListener onUserClickListener;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setShowLoadMore(boolean show) {
        showLoadMore = show;
        notifyDataSetChanged();
    }

    public void addMoreUsers(List<User> newUsers) {
        int start = userList.size();
        userList.addAll(newUsers);
        notifyItemRangeInserted(start, newUsers.size());
    }

    public void setOnLoadMoreClickListener(OnLoadMoreClickListener listener) {
        this.loadMoreClickListener = listener;
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.onUserClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadMore && position == userList.size()) {
            return VIEW_TYPE_LOAD_MORE;
        }
        return VIEW_TYPE_USER;
    }

    @Override
    public int getItemCount() {
        return showLoadMore ? userList.size() + 1 : userList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_loadmore, parent, false);
            return new LoadMoreViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
            return new UserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            User user = userList.get(position);
            ((UserViewHolder) holder).bind(user);

            holder.itemView.setOnClickListener(v -> {
                if (onUserClickListener != null) {
                    onUserClickListener.onUserClick(user); // Memanggil listener
                }
            });
        } else if (holder instanceof LoadMoreViewHolder) {
            ((LoadMoreViewHolder) holder).bind();
        }
    }


    //// Menangani 2 view: karakter & tombol "Load More"

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, species;
        ImageView image;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            species = itemView.findViewById(R.id.species);
            image = itemView.findViewById(R.id.image);
        }

        public void bind(User user) {
            name.setText(user.getName());
            species.setText(user.getSpecies());
            Glide.with(itemView.getContext()).load(user.getImage()).into(image);
        }
    }

    public void clearUsers() {
        userList.clear();
        notifyDataSetChanged();
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        Button loadMoreButton;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            loadMoreButton = itemView.findViewById(R.id.button);
        }

        public void bind() {
            loadMoreButton.setOnClickListener(v -> {
                if (loadMoreClickListener != null) {
                    loadMoreClickListener.onLoadMoreClick();
                }
            });
        }
    }

    public interface OnLoadMoreClickListener {
        void onLoadMoreClick();
    }
}