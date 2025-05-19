package com.example.networking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private List<Character> characters = new ArrayList<>();
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Character character);
    }

    public CharacterAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setCharacters(List<Character> newCharacters) {
        this.characters.clear();
        this.characters.addAll(newCharacters);
        notifyDataSetChanged(); // Atau gunakan DiffUtil untuk performa lebih baik
    }

    public void addCharacters(List<Character> moreCharacters) {
        int startPosition = characters.size();
        this.characters.addAll(moreCharacters);
        notifyItemRangeInserted(startPosition, moreCharacters.size());
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_character_card, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = characters.get(position);
        holder.bind(character, listener);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        ImageView characterImage;
        TextView characterName;
        TextView characterSpecies;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            characterImage = itemView.findViewById(R.id.character_image);
            characterName = itemView.findViewById(R.id.character_name);
            characterSpecies = itemView.findViewById(R.id.character_species);
        }

        public void bind(final Character character, final OnItemClickListener listener) {
            characterName.setText(character.getName());
            characterSpecies.setText(character.getSpecies());

            Glide.with(itemView.getContext())
                    .load(character.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(characterImage);

            itemView.setOnClickListener(v -> listener.onItemClick(character));
        }
    }
}