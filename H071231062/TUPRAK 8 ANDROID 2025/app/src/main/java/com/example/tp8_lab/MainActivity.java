package com.example.tp8_lab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView listView;
    private EditText searchEditText;
    private TextView noDataTextView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.searchEditText);
        noDataTextView = findViewById(R.id.noDataTextView);

        // buat search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("ITEM_ID", id);
                startActivity(intent);
            }
        });


        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivity(intent);
            }
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();  // Reload data
    }

    private void loadData() {
        Cursor cursor = dbHelper.getAllData();

        // menampilkan no data ketika baru meload
        if (cursor.getCount() == 0) {
            noDataTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            String[] fromColumns = {"title", "timestamp"};
            int[] toViews = {R.id.titleTextView, R.id.timestampTextView};

            adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.list_item,
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            listView.setAdapter(adapter);
        }
    }

    private void searchData(String query) {
        Cursor cursor = dbHelper.searchByTitle(query);

        // menampilikan no data bila search dan tidak ada data yang sama
        if (cursor.getCount() == 0) {
            noDataTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            String[] fromColumns = {"title", "timestamp"};
            int[] toViews = {R.id.titleTextView, R.id.timestampTextView};

            adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.list_item,
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            listView.setAdapter(adapter);
        }
    }
}