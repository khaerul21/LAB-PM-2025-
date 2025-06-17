package com.example.mylibraryapp.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Book>> bookListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Book>> favoriteBooks = new MutableLiveData<>();
    private final List<Book> internalBookList; // Disimpan secara internal di memory aplikasi
    private final MutableLiveData<List<Book>> filteredBooksLiveData = new MutableLiveData<>(); // Untuk hasil pencarian

    public BookViewModel(@NonNull Application application) {
        super(application);
        // Inisialisasi hanya sekali dari sumber statis BookData
        internalBookList = new ArrayList<>(BookData.getBooks());
        bookListLiveData.setValue(internalBookList);
    }

    // Mendapatkan semua buku
    public LiveData<List<Book>> getBookList() {
        return bookListLiveData;
    }

    // Toggle status buku dan perbarui daftar
    public void toggleBookStatus(Book targetBook) {
        List<Book> books = BookData.getBooks();
        for (Book book : books) {
            if (book.getTitle().equals(targetBook.getTitle())) {
                book.setBookStatus(!book.getBookStatus());
                break;
            }
        }
    }


    // Mendapatkan buku favorit, disimpan dalam LiveData yang sama
    public LiveData<List<Book>> getFavoriteBooks() {
        // Filter buku favorit berdasarkan status
        List<Book> favoriteBooks = new ArrayList<>();
        for (Book book : internalBookList) {
            if (book.getBookStatus()) {  // Menggunakan status untuk menentukan favorit
                favoriteBooks.add(book);
            }
        }

        // Memperbarui nilai LiveData dengan daftar buku favorit
        bookListLiveData.setValue(new ArrayList<>(favoriteBooks));
        return bookListLiveData;
    }

    public void refreshFavoriteBooks() {
        List<Book> favorites = new ArrayList<>();
        for (Book book : BookData.getBooks()) {
            if (book.getBookStatus()) {
                favorites.add(book);
            }
        }
        favoriteBooks.setValue(favorites);
    }


    // Fungsi pencarian buku berdasarkan query
    public void findBooksWithSearch(String query) {
        // Filter buku berdasarkan query
        List<Book> result = new ArrayList<>();
        for (Book book : internalBookList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                result.add(book);
            }
        }

        // Memperbarui LiveData dengan hasil pencarian
        filteredBooksLiveData.setValue(result);
    }

    // Mendapatkan hasil pencarian (LiveData)
    public LiveData<List<Book>> getFilteredBooks() {
        return filteredBooksLiveData;
    }

}
