package com.example.t4_prak.ui.addbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.t4_prak.Book;
import com.example.t4_prak.BookRepository;
import com.example.t4_prak.R;

public class AddBookFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "AddBookFragment";

    private EditText inputTitle, inputAuthor, inputYear, inputBlurb, inputGenre;
    private RatingBar inputRating;
    private ImageView coverPreview;
    private Uri selectedCoverUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_book, container, false);


        inputTitle = root.findViewById(R.id.input_title);
        inputAuthor = root.findViewById(R.id.input_author);
        inputYear = root.findViewById(R.id.input_year);
        inputBlurb = root.findViewById(R.id.input_blurb);
        inputGenre = root.findViewById(R.id.input_genre);
        inputRating = root.findViewById(R.id.input_rating);
        coverPreview = root.findViewById(R.id.image_cover_preview);
        Button saveBookButton = root.findViewById(R.id.button_save_book);

        // buat pilih gambar dari galeri
        coverPreview.setOnClickListener(v -> openGallery());


        saveBookButton.setOnClickListener(v -> saveBook());

        return root;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            selectedCoverUri = data.getData();
            coverPreview.setImageURI(selectedCoverUri); // Preview the selected image
            Log.d(TAG, "Selected URI: " + selectedCoverUri.toString());
        } else {
            Log.d(TAG, "No image selected or invalid data.");
        }
    }

    private void saveBook() {
        String title = inputTitle.getText().toString().trim();
        String author = inputAuthor.getText().toString().trim();
        String yearText = inputYear.getText().toString().trim();
        String blurb = inputBlurb.getText().toString().trim();
        String genre = inputGenre.getText().toString().trim();
        float rating = inputRating.getRating();

        // Validate inputs
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(yearText) || TextUtils.isEmpty(blurb) || TextUtils.isEmpty(genre)) {
            Toast.makeText(getContext(), "Silahkan Isi Semua Terlebih Dahulu!.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCoverUri == null) {
            Toast.makeText(getContext(), "Silahkan Pilih Gambar Anda", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Tahun Tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Add new book to repository
            Book newBook = new Book(title, author, year, blurb, selectedCoverUri.toString(), false, genre, rating);
            BookRepository.getInstance().getBooks().add(0, newBook);

            Toast.makeText(getContext(), "Buku Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();

            // Clear inputs
            inputTitle.setText("");
            inputAuthor.setText("");
            inputYear.setText("");
            inputBlurb.setText("");
            inputGenre.setText("");
            inputRating.setRating(0);
            coverPreview.setImageDrawable(null);
            selectedCoverUri = null;

        } catch (Exception e) {
            Log.e(TAG, "Error saving book: ", e);
            Toast.makeText(getContext(), "Failed to save book. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}