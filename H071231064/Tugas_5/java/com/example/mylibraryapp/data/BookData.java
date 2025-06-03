package com.example.mylibraryapp.data;

import com.example.mylibraryapp.R;

import java.util.ArrayList;
import java.util.List;

public class BookData {
    // Gunakan satu list statis untuk menyimpan data buku
    private static final List<Book> books = new ArrayList<>();

    // Inisialisasi hanya sekali
    static {
        int imageResId = R.drawable.cover_buku;

        books.add(new Book("ImageResId", "Sang Pemimpi", "Andrea Hirata", 2006, "Kisah inspiratif tentang mimpi dan harapan.", false, 10));
        books.add(new Book("ImageResId", "Laskar Pelangi", "Andrea Hirata", 2005, "Perjuangan anak-anak Belitung mengejar pendidikan.", false, 12));
        books.add(new Book("ImageResId", "Negeri 5 Menara", "Ahmad Fuadi", 2009, "Perjalanan santri dalam menuntut ilmu.", false, 8));
        books.add(new Book("ImageResId", "Bumi", "Tere Liye", 2014, "Petualangan dunia paralel dan kekuatan rahasia.", false, 6));
        books.add(new Book("ImageResId", "Perahu Kertas", "Dewi Lestari", 2008, "Cerita cinta dan pencarian jati diri.", false, 5));
        books.add(new Book("ImageResId", "Supernova", "Dewi Lestari", 2001, "Perpaduan sains, cinta, dan spiritualitas.", false, 9));
        books.add(new Book("ImageResId", "Ayat-Ayat Cinta", "Habiburrahman El Shirazy", 2004, "Cinta dan perjuangan hidup mahasiswa di Mesir.", false, 7));
        books.add(new Book("ImageResId", "Rantau 1 Muara", "Ahmad Fuadi", 2013, "Perjuangan mencari ilmu ke luar negeri.", false, 4));
        books.add(new Book("ImageResId", "Hujan", "Tere Liye", 2016, "Kisah cinta dan bencana alam.", false, 11));
        books.add(new Book("ImageResId", "Pulang", "Tere Liye", 2015, "Kisah agen rahasia dan pengkhianatan.", false, 3));
        books.add(new Book("ImageResId", "Tentang Kamu", "Tere Liye", 2016, "Penelusuran hidup seorang wanita luar biasa.", true, 2));
        books.add(new Book("ImageResId", "Garis Waktu", "Fiersa Besari", 2016, "Perjalanan waktu, cinta, dan kenangan.", false, 15));
        books.add(new Book("ImageResId", "Kambing Jantan", "Raditya Dika", 2005, "Komedi kehidupan mahasiswa di luar negeri.", false, 6));
        books.add(new Book("ImageResId", "Koala Kumal", "Raditya Dika", 2015, "Kisah patah hati dan penemuan cinta baru.", false, 5));
        books.add(new Book("ImageResId", "Critical Eleven", "Ika Natassa", 2015, "Drama cinta pasangan modern.", true, 7));
    }

    // Ambil semua buku
    public static List<Book> getBooks() {
        return books;
    }

    // Ambil hanya buku yang isLiked == true
    public static List<Book> getFavoriteBooks() {
        List<Book> favorites = new ArrayList<>();
        for (Book book : books) {
            if (book.getBookStatus()) {
                favorites.add(book);
            }
        }
        return favorites;
    }

    public static void addBook(Book book) {
        books.add(book);
    }

}
