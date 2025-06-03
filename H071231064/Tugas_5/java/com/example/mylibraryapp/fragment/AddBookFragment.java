package com.example.mylibraryapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mylibraryapp.R;
import com.example.mylibraryapp.data.Book;
import com.example.mylibraryapp.data.BookData;

import java.io.IOException;

public class AddBookFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgBookPreview;
    private Uri selectedImageUri;

    private EditText etJudul, etPenulis, etTahun, etDeskripsi;

    public AddBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        // Inisialisasi view
        imgBookPreview = view.findViewById(R.id.img_book_preview);
        Button btnSelectImage = view.findViewById(R.id.btn_select_image);
        Button btnSimpan = view.findViewById(R.id.btn_simpan);

        etJudul = view.findViewById(R.id.et_judul);
        etPenulis = view.findViewById(R.id.et_penulis);
        etTahun = view.findViewById(R.id.et_tahun);
        etDeskripsi = view.findViewById(R.id.et_blurb);

        // Pilih gambar dari galeri
        btnSelectImage.setOnClickListener(v -> openGallery());

        // Simpan data buku
        btnSimpan.setOnClickListener(v -> saveBook());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveBook() {
        String judul = etJudul.getText().toString().trim();
        String penulis = etPenulis.getText().toString().trim();
        String tahun = etTahun.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (judul.isEmpty() || penulis.isEmpty() || tahun.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(getContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int tahunTerbit = Integer.parseInt(tahun);

        // Create a new Book object using the provided details
        Book book = new Book(
                selectedImageUri != null ? selectedImageUri.toString() : null, // Image URL as string
                judul,  // Book title
                penulis, // Author
                tahunTerbit, // Year of publication
                deskripsi, // Description
                false,  // Initial liked status (false)
                0 // Stock (using int type, so set it to 0)
        );

        BookData.addBook(book);


        Toast.makeText(getContext(), "Buku berhasil ditambahkan!", Toast.LENGTH_SHORT).show();

        // Kosongkan form setelah simpan
        etJudul.setText("");
        etPenulis.setText("");
        etTahun.setText("");
        etDeskripsi.setText("");
        imgBookPreview.setImageResource(R.drawable.ic_launcher_background); // Atur default kembali
        selectedImageUri = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                imgBookPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
