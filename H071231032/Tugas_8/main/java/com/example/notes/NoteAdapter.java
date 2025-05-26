package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private Context context;
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public NoteAdapter(Context context, List<Note> noteList, OnNoteClickListener listener) {
        this.context = context;
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = noteList.get(position);

        // Format tanggal
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String createdAtDate = sdf.format(new Date(currentNote.getCreatedAt()));
        String updatedAtDate = sdf.format(new Date(currentNote.getUpdatedAt()));

        boolean isUpdated = currentNote.getUpdatedAt() > currentNote.getCreatedAt();

        if (isUpdated) {
            holder.textViewTimestamp.setText(String.format("Updated at: %s", updatedAtDate));
        } else {
            holder.textViewTimestamp.setText(String.format("Created at: %s", createdAtDate));
        }

        String titleText = currentNote.getTitle();
        String descriptionText = currentNote.getDescription();

        if (isUpdated) {
            if (currentNote.wasTitleChangedInLastUpdate()) {
                titleText += " (updated)";
            }
            if (currentNote.wasDescriptionChangedInLastUpdate()) {
                descriptionText += " (updated)";
            }
        }

        holder.textViewTitle.setText(titleText);
        holder.textViewDescription.setText(descriptionText);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoteClick(currentNote);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Note> filteredList) {
        noteList = filteredList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotes(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }


    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewTimestamp;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTimestamp = itemView.findViewById(R.id.text_view_timestamp);
            textViewTitle = itemView.findViewById(R.id.text_view_title_item);
            textViewDescription = itemView.findViewById(R.id.text_view_description_item);
        }
    }
}