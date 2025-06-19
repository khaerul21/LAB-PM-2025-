package com.example.tp4jegel20.data;

import android.util.Log;
import com.example.tp4jegel20.model.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private final List<Book> books;
    private static final String TAG = "BookRepository";

    private BookRepository() {
        books = new ArrayList<>(Arrays.asList(
                new Book("Haerin in My Dreams", "King Rey", 2025,
                        "Petualangan mimpi yang dipenuhi senyum Haerin.",
                        "https://i.pinimg.com/736x/bb/5d/cc/bb5dcc10757bd75d2950a615c1a9b03c.jpg",
                        "Fantasy Romance"),

                new Book("Lovable Haerin", "Master Rey", 2024,
                        "Kehidupan sehari-hari bersama Haerin yang manis dan menghangatkan hati.",
                        "https://i.pinimg.com/736x/7e/46/78/7e4678db4df161b5e9dc8267ce520709.jpg",
                        "Slice of Life"),

                new Book("Haerin in Another World", "Doctor Rey", 2025,
                        "Isekai penuh sihir dan rahasia, dengan Haerin sebagai pahlawan utama.",
                        "https://i.pinimg.com/736x/fb/8d/29/fb8d290ba9448b9d961fa1bff702cfd9.jpg",
                        "Fantasy Adventure"),

                new Book("Haerin, My Wife", "Rey 'Haerin's Husband'", 2025,
                        "Kisah cinta manis dan penuh perjuangan bersama Haerin.",
                        "https://i.pinimg.com/736x/91/ba/16/91ba16ae59e0b62a20c5a11ca8eca7af.jpg",
                        "Romance"),

                new Book("The Code of Haerin", "Agent Rey", 2025,
                        "Misteri dunia tersembunyi yang berkaitan dengan sejarah Haerin.",
                        "https://i.pinimg.com/736x/32/38/f2/3238f2b9eafaf734f2fa49586256172e.jpg",
                        "Mystery Thriller"),

                new Book("Searching for Haerin", "Explorer Rey", 2025,
                        "Ekspedisi penuh tantangan untuk menemukan keberadaan Haerin yang hilang.",
                        "https://upload.wikimedia.org/wikipedia/commons/e/e2/2023_MMA_NewJeans_Haerin_1.jpg",
                        "Adventure"),

                new Book("Forgiving Haerin", "Rey of Hope", 2025,
                        "Drama emosional tentang kehilangan, harapan, dan pengampunan.",
                        "https://upload.wikimedia.org/wikipedia/commons/1/1c/NewJeans_Haerin_Incheon_Airport_2.jpg",
                        "Drama"),

                new Book("Agent Haerin", "Shadow Rey", 2025,
                        "Haerin adalah agen rahasia yang sedang dalam misi penting dunia.",
                        "https://upload.wikimedia.org/wikipedia/commons/7/7b/NewJeans_HAERIN_Dior_2.jpg",
                        "Action"),

                new Book("The Mind of Haerin", "Philosopher Rey", 2025,
                        "Biografi fiksi tentang kecerdasan dan kepribadian Haerin.",
                        "https://upload.wikimedia.org/wikipedia/commons/e/ed/Haerin_Seoul_Fashion_Week_1.jpg",
                        "Biography"),

                new Book("Haerin: Sands of Time", "Timekeeper Rey", 2025,
                        "Epik futuristik di mana Haerin mengendalikan waktu dan ruang.",
                        "https://upload.wikimedia.org/wikipedia/commons/b/be/NewJeans_OLensglobal.jpg",
                        "Science Fiction"),

                new Book("Haerin’s Magical Kitchen", "Chef Rey", 2025,
                        "Masakan penuh cinta dan keajaiban dari tangan Haerin.",
                        "https://upload.wikimedia.org/wikipedia/commons/a/a7/Haerin_Seoul_Fashion_Week_3.jpg",
                        "Culinary Fiction"),

                new Book("Haerin's Survival Games", "Commander Rey", 2025,
                        "Haerin berjuang untuk bertahan dalam dunia distopia penuh bahaya.",
                        "https://upload.wikimedia.org/wikipedia/commons/8/8c/20230905_Haerin_%28NewJeans%29.jpg",
                        "Dystopian"),

                new Book("Pride and Haerin", "Sir Rey", 2025,
                        "Romansa klasik bergaya Inggris dengan sentuhan pesona Haerin.",
                        "https://upload.wikimedia.org/wikipedia/commons/8/87/Haerin_%28NewJeans%29_220813.jpg",
                        "Romance"),

                new Book("The Path of Haerin", "Wanderer Rey", 2025,
                        "Perjalanan spiritual dan takdir bersama cahaya Haerin.",
                        "https://upload.wikimedia.org/wikipedia/commons/a/a7/Haerin_Seoul_Fashion_Week_3.jpg",
                        "Fantasy"),

                new Book("Haerin in Suburbia", "Neighbor Rey", 2025,
                        "Kehidupan tenang yang perlahan berubah saat Haerin hadir.",
                        "https://upload.wikimedia.org/wikipedia/commons/b/be/NewJeans_OLensglobal.jpg",
                        "Drama")
        ));
    }




    public static BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getFavoriteBooks() {
        List<Book> favoriteBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isFavorite()) {
                favoriteBooks.add(book);
            }
        }
        return favoriteBooks;
    }


    public void addBook(Book book) {
        Log.d(TAG, "Adding new book: " + book.getTitle());
        books.add(0, book); // Add to beginning of list
        Log.d(TAG, "Total books after adding: " + books.size());

    }

    public void updateBook(Book book) {
        int index = books.indexOf(book);
        if (index != -1) {
            books.set(index, book);
        }
    }

    public void toggleFavorite(Book book) {
        int index = -1;
        // Cari buku berdasarkan title dan author
        for (int i = 0; i < books.size(); i++) {
            Book currentBook = books.get(i);
            if (currentBook.getTitle().equals(book.getTitle()) &&
                    currentBook.getAuthor().equals(book.getAuthor())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            Book existingBook = books.get(index);
            boolean newStatus = !existingBook.isFavorite();
            existingBook.setFavorite(newStatus);
            books.set(index, existingBook);
            Log.d(TAG, "Book favorite status changed - Title: " + existingBook.getTitle() +
                    ", New status: " + newStatus);
        } else {
            Log.w(TAG, "Book not found in repository: " + book.getTitle());
        }
    }

    public List<Book> searchBooks(String query) {
        List<Book> filteredBooks = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                    book.getAuthor().toLowerCase().contains(lowerQuery) ||
                    book.getGenre().toLowerCase().contains(lowerQuery)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
}