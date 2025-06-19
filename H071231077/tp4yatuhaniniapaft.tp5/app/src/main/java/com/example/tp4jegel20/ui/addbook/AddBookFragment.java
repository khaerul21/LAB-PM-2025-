package com.example.tp4jegel20.ui.addbook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tp4jegel20.R;
import com.example.tp4jegel20.data.BookRepository;
import com.example.tp4jegel20.model.Book;
import com.google.android.material.textfield.TextInputEditText;

public class AddBookFragment extends Fragment {
    private static final String TAG = "AddBookFragment";
    private ImageView imageBook;
    private TextInputEditText editTitle, editAuthor, editYear, editBlurb, editGenre; // Tambah editGenre
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        selectedImageUri = data.getData();
                        Glide.with(this)
                                .load(selectedImageUri)
                                .placeholder(R.drawable.book_placeholder)
                                .error(R.drawable.book_error)
                                .into(imageBook);
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        imageBook = view.findViewById(R.id.imageBook);
        editTitle = view.findViewById(R.id.editTitle);
        editAuthor = view.findViewById(R.id.editAuthor);
        editYear = view.findViewById(R.id.editYear);
        editBlurb = view.findViewById(R.id.editBlurb);
        editGenre = view.findViewById(R.id.editGenre); // Tambahkan ini ke layout
        Button buttonSelectImage = view.findViewById(R.id.buttonSelectImage);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);

        // Set default image
        imageBook.setImageResource(R.drawable.book_placeholder);

        buttonSelectImage.setOnClickListener(v -> openImagePicker());
        buttonAdd.setOnClickListener(v -> addBook());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.launch(intent);
    }

    private void addBook() {
        // Get values with null checks
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String author = editAuthor.getText() != null ? editAuthor.getText().toString().trim() : "";
        String yearStr = editYear.getText() != null ? editYear.getText().toString().trim() : "";
        String blurb = editBlurb.getText() != null ? editBlurb.getText().toString().trim() : "";
        String genre = editGenre.getText() != null ? editGenre.getText().toString().trim() : "General";

        // Validation
        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Year validation
        int year;
        try {
            year = Integer.parseInt(yearStr);
            if (year < 1000 || year > 2025) {
                Toast.makeText(getContext(), "Please enter a valid year (1000-2025)", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid year format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new book
        Book newBook = new Book(
                title,
                author,
                year,
                blurb.isEmpty() ? "No description available" : blurb,
                selectedImageUri != null ? selectedImageUri.toString() :
                        "https://via.placeholder.com/150",
                genre.isEmpty() ? "General" : genre
        );

        // Add to repository
        try {
            BookRepository.getInstance().addBook(newBook);
            Log.d(TAG, "Book added successfully: " + newBook.getTitle());

            // Show success message
            Toast.makeText(getContext(), "Book added successfully", Toast.LENGTH_SHORT).show();

            // Clear form
            clearForm();

        } catch (Exception e) {
            Log.e(TAG, "Error adding book: " + e.getMessage());
            Toast.makeText(getContext(), "Error adding book", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        editTitle.setText("");
        editAuthor.setText("");
        editYear.setText("");
        editBlurb.setText("");
        editGenre.setText("");
        imageBook.setImageResource(R.drawable.book_placeholder);
        selectedImageUri = null;
    }
}