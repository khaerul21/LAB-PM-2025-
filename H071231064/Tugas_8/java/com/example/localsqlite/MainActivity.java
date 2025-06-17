package com.example.localsqlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;
    private TextView noData;
    private final int REQUEST_ADD = 100;
    private final int REQUEST_UPDATE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Note List");
        }
        rvNotes = findViewById(R.id.rv_students);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        noData = findViewById(R.id.noData);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this);
        rvNotes.setAdapter(adapter);
        noteHelper = NoteHelper.getInstance(getApplicationContext());
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });
        EditText etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNotes(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        loadNotes();
    }
    private void loadNotes() {
        new LoadNotesAsync(this, notes -> {
            if (notes.size() > 0) {
                adapter.setNotes(notes);
                noData.setVisibility(View.GONE);
            } else {
                adapter.setNotes(new ArrayList<>());
                noData.setVisibility(View.VISIBLE);
            }
        }).execute();
    }
    private void searchNotes(String query) {
        new LoadNotesAsync(this, notes -> {
            ArrayList<Note> filtered = new ArrayList<>();
            for (Note n : notes) {
                if (n.getJudul().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(n);
                }
            }
            if (filtered.size() > 0) {
                adapter.setNotes(filtered);
                noData.setVisibility(View.GONE);
            } else {
                adapter.setNotes(new ArrayList<>());
                noData.setVisibility(View.VISIBLE);
            }
        }).execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD) {
            if (resultCode == FormActivity.RESULT_ADD) {

                showToast("Note added successfully");
                loadNotes();
            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == FormActivity.RESULT_UPDATE) {
                showToast("Note updated successfully");
                loadNotes();
            } else if (resultCode == FormActivity.RESULT_DELETE) {
                showToast("Note deleted successfully");
                loadNotes();
            }
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }
    private static class LoadNotesAsync {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;
        private LoadNotesAsync(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }
        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                Context context = weakContext.get();
                if (context != null) {
                    NoteHelper noteHelper = NoteHelper.getInstance(context);
                    noteHelper.open();
                    Cursor notesCursor = noteHelper.queryAll();
                    ArrayList<Note> notes = MappingHelper.mapCursorToArrayList(notesCursor);
                    notesCursor.close();
                    handler.post(() -> {
                        LoadNotesCallback callback = weakCallback.get();
                        if (callback != null) {
                            callback.postExecute(notes);
                        }
                    });
                }
            });
        }
    }
    interface LoadNotesCallback {
        void postExecute(ArrayList<Note> notes);
    }
}
