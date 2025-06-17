package com.example.tp8_lab;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private TextView timestampTextView;
    private Button saveButton;
    private Button deleteButton;

    private long itemId = -1;
    private boolean isUpdateOperation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper = new DatabaseHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        timestampTextView = findViewById(R.id.timestampTextView);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Check updated item
        if (getIntent().hasExtra("ITEM_ID")) {
            itemId = getIntent().getLongExtra("ITEM_ID", -1);
            if (itemId != -1) {
                isUpdateOperation = true;
                loadItemData(itemId);
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
                timestampTextView.setVisibility(View.GONE);
            }
        } else {

            deleteButton.setVisibility(View.GONE);
            timestampTextView.setVisibility(View.GONE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }

    private void loadItemData(long id) {
        Cursor cursor = dbHelper.getDataById(id);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP));

            titleEditText.setText(title);
            descriptionEditText.setText(description);
            timestampTextView.setText(timestamp);
            timestampTextView.setVisibility(View.VISIBLE);
        }
        cursor.close();
    }

    private void saveItem() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isUpdateOperation) {
            dbHelper.updateData(itemId, title, description);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.insertData(title, description);
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteItem() {
        if (itemId != -1) {
            dbHelper.deleteData(itemId);
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}