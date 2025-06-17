package com.example.rickmorty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    public interface OnCharacterClickListener {
        void onCharacterClick(int characterId);
    }

    private final List<RickMortyApi.Character> characters;
    private final OnCharacterClickListener listener;

    public CharacterAdapter(List<RickMortyApi.Character> characters, OnCharacterClickListener listener) {
        this.characters = characters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        RickMortyApi.Character character = characters.get(position);
        holder.name.setText(character.name);
        Glide.with(holder.itemView.getContext()).load(character.image).into(holder.image);

        if (character.status.equalsIgnoreCase("Alive")) {
            holder.statusDot.setBackgroundResource(R.drawable.green_dot);
        } else {
            holder.statusDot.setBackgroundResource(R.drawable.red_dot);
        }

        holder.statusSpecies.setText(character.status + " - " + character.species);
        holder.origin.setText(character.origin.name);
        holder.location.setText(character.location.name);

        holder.itemView.setOnClickListener(v -> listener.onCharacterClick(character.id));
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void addCharacters(List<RickMortyApi.Character> newCharacters) {
        int startPosition = characters.size();
        characters.addAll(newCharacters);
        notifyItemRangeInserted(startPosition, newCharacters.size());
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        View statusDot;
        TextView statusSpecies;
        TextView origin;
        TextView location;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.character_name);
            image = itemView.findViewById(R.id.character_image);
            statusDot = itemView.findViewById(R.id.character_status_dot);
            statusSpecies = itemView.findViewById(R.id.character_status_species);
            origin = itemView.findViewById(R.id.character_origin);
            location = itemView.findViewById(R.id.character_location);
        }
    }
}
