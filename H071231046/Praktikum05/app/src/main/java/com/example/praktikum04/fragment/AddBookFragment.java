package com.example.praktikum04.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.praktikum04.R;
import com.example.praktikum04.activity.DetailActivity;
import com.example.praktikum04.models.Book;
import com.example.utils.BookDummy;


public class AddBookFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etTitle, etAuthor, etGenre, etDescription, etBlurb, etYear;
    private ImageView ivCoverPreview;
    private Button btnSelectImage, btnAddBook;

    private Uri selectedImageUri;

    public AddBookFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        etTitle = view.findViewById(R.id.etTitle);
        etAuthor = view.findViewById(R.id.etAuthor);
        etGenre = view.findViewById(R.id.etGenre);
        etDescription = view.findViewById(R.id.etDescription);
        ivCoverPreview = view.findViewById(R.id.ivCoverPreview);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnAddBook = view.findViewById(R.id.btnAddBook);
        etBlurb = view.findViewById(R.id.etBlurb);
        etYear = view.findViewById(R.id.etYear);

        btnSelectImage.setOnClickListener(v -> openGallery());

        btnAddBook.setOnClickListener(v -> addBook());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ivCoverPreview.setImageURI(selectedImageUri);
        }
    }

    private void addBook() {
        String title = etTitle.getText().toString();
        String author = etAuthor.getText().toString();
        String genre = etGenre.getText().toString();
        String desc = etDescription.getText().toString();
        String blurb = etBlurb.getText().toString();
        int year = Integer.parseInt(etYear.getText().toString());
        float randomRating = 1 + (float)(Math.random() * 4);

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || desc.isEmpty() || selectedImageUri == null) {
            Toast.makeText(getContext(), "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageUriString = selectedImageUri.toString();

        Book newBook = new Book(
                title,
                author,
                genre,
                -1,                   // coverResId tidak digunakan
                desc,
                imageUriString,       // ini dari galeri
                year,
                blurb,
                false,
                randomRating
        );
        BookDummy.bookList.add(0, newBook);

        Toast.makeText(getContext(), "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("book", newBook);
        startActivity(intent);

        // Reset form
        etTitle.setText("");
        etAuthor.setText("");
        etGenre.setText("");
        etDescription.setText("");
        ivCoverPreview.setImageResource(R.drawable.ic_launcher_background);
        selectedImageUri = null;
        etBlurb.setText("");
        etYear.setText("");


    }


}
