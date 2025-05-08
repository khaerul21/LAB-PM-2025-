package com.example.e_library.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.e_library.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.e_library.R;
import com.example.e_library.models.Book;
import com.example.e_library.utils.BookData;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddBookFragment extends Fragment {
    private ImageView ivCover;
    private TextInputLayout tilTitle, tilAuthor, tilYear, tilBlurb, tilGenre, tilRating;
    private TextInputEditText etTitle, etAuthor, etYear, etBlurb, etRating;
    private Spinner spinnerGenre;
    private Uri selectedImageUri = null;
    private FrameLayout loadingOverlay;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    // Tampilkan gambar yang dipilih menggunakan Glide
                    Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.ic_add_photo)
                            .error(R.drawable.ic_add_photo)
                            .into(ivCover);
                }
            });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi Views
        ivCover = view.findViewById(R.id.iv_add_cover);
        Button btnSelectImage = view.findViewById(R.id.btn_select_image);
        Button btnAddBook = view.findViewById(R.id.btn_add_book);
        tilTitle = view.findViewById(R.id.til_title);
        tilAuthor = view.findViewById(R.id.til_author);
        tilYear = view.findViewById(R.id.til_year);
        tilBlurb = view.findViewById(R.id.til_blurb);
        tilGenre = view.findViewById(R.id.til_genre);
        tilRating = view.findViewById(R.id.til_rating);
        etTitle = view.findViewById(R.id.et_title);
        etAuthor = view.findViewById(R.id.et_author);
        etYear = view.findViewById(R.id.et_year);
        etBlurb = view.findViewById(R.id.et_blurb);
        etRating = view.findViewById(R.id.et_rating);
        spinnerGenre = view.findViewById(R.id.spinner_genre);
        ArrayAdapter<String> genreAdapter = getStringArrayAdapter();
        spinnerGenre.setAdapter(genreAdapter);
        loadingOverlay = view.findViewById(R.id.loading_overlay);

        // Listener untuk memilih gambar
        btnSelectImage.setOnClickListener(v -> openGallery());
        ivCover.setOnClickListener(v -> openGallery());

        // Listener untuk tombol tambah buku
        btnAddBook.setOnClickListener(v -> addBook());
    }

    @NonNull
    private ArrayAdapter<String> getStringArrayAdapter() {
        List<String> allGenres = BookData.getAllGenres();
        return new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, allGenres) {
            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundResource(R.drawable.bg_spinner_item);
                return view;
            }
        };
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }


    private void addBook() {
        String title = Objects.requireNonNull(etTitle.getText()).toString().trim();
        String author = Objects.requireNonNull(etAuthor.getText()).toString().trim();
        String yearStr = Objects.requireNonNull(etYear.getText()).toString().trim();
        String blurb = Objects.requireNonNull(etBlurb.getText()).toString().trim();
        String genre = spinnerGenre.getSelectedItem() != null ? spinnerGenre.getSelectedItem().toString() : "";
        String ratingStr = Objects.requireNonNull(etRating.getText()).toString().trim();

        // --- Validasi Input ---
        boolean isValid = true;

        if (selectedImageUri == null) {
            selectedImageUri = Uri.parse("android.resource://com.example.e_library/" + R.drawable.placeholder_cover);
        }

        if (TextUtils.isEmpty(title)) {
            tilTitle.setError("Judul tidak boleh kosong");
            isValid = false;
        } else {
            tilTitle.setError(null);
        }

        if (TextUtils.isEmpty(author)) {
            tilAuthor.setError("Penulis tidak boleh kosong");
            isValid = false;
        } else {
            tilAuthor.setError(null);
        }

        if (TextUtils.isEmpty(yearStr)) {
            tilYear.setError("Tahun terbit tidak boleh kosong");
            isValid = false;
        } else if (Integer.parseInt(yearStr) > 2025 || Integer.parseInt(yearStr) < 1900) {
            tilYear.setError("Hanya menerima interval tahun terbit 1900–2025");
            isValid = false;
        } else {
            try {
                Integer.parseInt(yearStr); // Cek apakah angka valid
                tilYear.setError(null);
            } catch (NumberFormatException e) {
                tilYear.setError("Tahun tidak valid");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(blurb)) {
            tilBlurb.setError("Blurb atau deskripsi singkat tidak boleh kosong");
            isValid = false;
        } else {
            tilBlurb.setError(null);
        }

        if (TextUtils.isEmpty(genre)) {
            tilGenre.setError("Genre harus dipilih");
            isValid = false;
        } else {
            tilGenre.setError(null);
        }

        float rating = 0.0f;
        if (!TextUtils.isEmpty(ratingStr)) {
            try {
                rating = Float.parseFloat(ratingStr);
                if (rating < 0.0f || rating > 5.0f) {
                    tilRating.setError("Rating harus antara 0.0–5.0");
                    isValid = false;
                } else {
                    tilRating.setError(null);
                }
            } catch (NumberFormatException e) {
                tilRating.setError("Rating tidak valid");
                isValid = false;
            }
        } else {
            // Rating bersifat opsional, jika kosong set null/default
            tilRating.setError(null);
        }

        if (!isValid) {
            return;
        }

        // --- Buat Objek Buku Baru ---
        loadingOverlay.setVisibility(View.VISIBLE);
        float finalRating = rating;
        executor.execute(() -> {
            try {
                int year = Integer.parseInt(yearStr);
                long newId = BookData.getNewId(); // ID unik baru
                Book newBook = new Book(
                        newId,
                        title,
                        author,
                        year,
                        blurb,
                        selectedImageUri,
                        genre,
                        finalRating
                );
                mainHandler.postDelayed(() -> {
                    BookData.addBook(newBook);
                    Toast.makeText(getContext(), "Buku '" + title + "' berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    resetForm();
                    loadingOverlay.setVisibility(View.GONE);
                    ((MainActivity) requireActivity()).navigateToHome();
                }, 1000);
            } catch (Exception e) {
                e.getStackTrace();
                requireActivity().runOnUiThread(() -> {
                    loadingOverlay.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Gagal menambahkan buku", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void resetForm() {
        etTitle.setText("");
        etAuthor.setText("");
        etYear.setText("");
        etBlurb.setText("");
        spinnerGenre.setSelection(0);
        etRating.setText("");
        selectedImageUri = null;
        ivCover.setImageResource(R.drawable.ic_add_photo);
        tilTitle.setError(null);
        tilAuthor.setError(null);
        tilYear.setError(null);
        tilRating.setError(null);
        etTitle.clearFocus();
    }
}