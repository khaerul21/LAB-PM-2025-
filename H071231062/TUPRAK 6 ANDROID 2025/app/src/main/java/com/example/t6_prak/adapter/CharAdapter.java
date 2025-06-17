package com.example.t6_prak.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.t6_prak.DetailActivity;
import com.example.t6_prak.R;
import com.example.t6_prak.model.Character;
import com.example.t6_prak.network.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CharAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOAD_MORE = 1;

    private List<Character> characterList;
    private OnLoadMoreListener loadMoreListener;

    public CharAdapter(List<Character> characterList) {
        this.characterList = characterList;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    @Override
    public int getItemCount() {
        return characterList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == characterList.size()) {
            return VIEW_TYPE_LOAD_MORE;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.char_item, parent, false);
            return new CharacterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.load_more, parent, false);
            return new LoadMoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CharacterViewHolder) {
            Character character = characterList.get(position);
            CharacterViewHolder characterHolder = (CharacterViewHolder) holder;
            characterHolder.nameTextView.setText(character.getName());
            characterHolder.speciesTextView.setText(character.getSpecies());
            Picasso.get().load(character.getImage()).into(characterHolder.imageView);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("name", character.getName());
                intent.putExtra("status", character.getStatus());
                intent.putExtra("species", character.getSpecies());
                intent.putExtra("gender", character.getGender());
                intent.putExtra("image", character.getImage());
                v.getContext().startActivity(intent);
            });

        } else if (holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder loadMoreHolder = (LoadMoreViewHolder) holder;
            loadMoreHolder.loadMoreButton.setOnClickListener(v -> {

                loadMoreHolder.loadMoreButton.setVisibility(View.INVISIBLE);
                loadMoreHolder.progressBar.setVisibility(View.VISIBLE);


                new Thread(() -> {
                    try {

                        Thread.sleep(1000);


                        v.post(() -> {
                            if (loadMoreListener != null) {
                                loadMoreListener.onLoadMoreClicked();
                            }

                            loadMoreHolder.loadMoreButton.setVisibility(View.VISIBLE);
                            loadMoreHolder.progressBar.setVisibility(View.GONE);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            });
        }
    }

    // ViewHolder untuk karakter
    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, speciesTextView;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            speciesTextView = itemView.findViewById(R.id.speciesTextView);
        }
    }

    // ViewHolder untuk tombol Load More
    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        Button loadMoreButton;
        ProgressBar progressBar;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            loadMoreButton = itemView.findViewById(R.id.btnLoadMore);
            progressBar = itemView.findViewById(R.id.loadMoreProgress);
        }
    }
}