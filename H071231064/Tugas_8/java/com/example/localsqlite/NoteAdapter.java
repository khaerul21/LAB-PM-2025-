package com.example.localsqlite;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final ArrayList<Note> notes = new ArrayList<>();
    private final Activity activity;
    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }
    public void setNotes(ArrayList<Note> notes) {
        this.notes.clear();
        if (notes.size() > 0) {
            this.notes.addAll(notes);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new NoteViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }
    class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvJudul, tvDeskripsi, tvTimestamp;
        final CardView cardView;
        NoteViewHolder(View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_item_name);
            tvDeskripsi = itemView.findViewById(R.id.tv_item_nim);
            tvTimestamp = itemView.findViewById(R.id.tv_item_timestamp);
            cardView = itemView.findViewById(R.id.card_view);
        }
        void bind(Note note) {
            tvJudul.setText(note.getJudul());
            tvDeskripsi.setText(note.getDeskripsi());
            tvTimestamp.setText(note.getUpdatedAt() != null ? "Updated At: " + note.getUpdatedAt() : "");
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra(FormActivity.EXTRA_NOTE, note);
                activity.startActivityForResult(intent, FormActivity.REQUEST_UPDATE);
            });
        }
    }
}