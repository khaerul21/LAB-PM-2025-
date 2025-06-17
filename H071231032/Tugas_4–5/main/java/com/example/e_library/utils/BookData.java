package com.example.e_library.utils;

import com.example.e_library.R;
import com.example.e_library.models.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BookData {

    private static final List<Book> books = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong(); // ID unik

    static {
        // 15 data dummy
        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Laskar Pelangi",
                "Andrea Hirata",
                2005,
                "Kisah inspiratif tentang sepuluh anak dari keluarga miskin di Belitung yang berjuang meraih pendidikan di tengah keterbatasan. Dengan semangat dan kegigihan, mereka membuktikan bahwa mimpi bisa dicapai oleh siapa saja.",
                R.drawable.laskar_pelangi,
                false,
                "Novel",
                4.5f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Bumi Manusia",
                "Pramoedya Ananta Toer",
                1980,
                "Mengisahkan Minke, seorang pemuda pribumi cerdas yang hidup di masa kolonial Belanda. Melalui kisah cintanya dengan Annelies, novel ini menggambarkan perjuangan melawan ketidakadilan dan diskriminasi.",
                R.drawable.bumi_manusia,
                true,
                "Sejarah",
                4.8f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Perahu Kertas",
                "Dee Lestari",
                2009,
                "Kugy dan Keenan, dua jiwa kreatif dengan mimpi besar, bertemu dalam perjalanan hidup yang penuh liku. Sebuah kisah tentang cinta, persahabatan, dan pencarian jati diri.",
                R.drawable.perahu_kertas,
                false,
                "Romance",
                4.2f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Cantik Itu Luka",
                "Eka Kurniawan",
                2002,
                "Dewi Ayu, seorang pelacur yang bangkit dari kematian, menjadi pusat kisah yang memadukan sejarah, mitos, dan realisme magis. Novel ini menggambarkan luka-luka sosial dan pribadi dalam masyarakat Indonesia.", 
                R.drawable.cantik_itu_luka,
                false,
                "Sastra",
                4.6f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Ayat-Ayat Cinta",
                "Habiburrahman El Shirazy",
                2004,
                "Fahri, mahasiswa Indonesia di Kairo, menghadapi dilema cinta dan iman dalam lingkungan yang penuh tantangan. Sebuah roman Islami yang menyentuh hati dan menggugah pikiran.",
                R.drawable.ayat_ayat_cinta,
                false,
                "Religi",
                4.4f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Negeri 5 Menara",
                "Ahmad Fuadi",
                2009,
                "Alif, pemuda dari Maninjau, menempuh pendidikan di pesantren Pondok Madani. Dengan motto \"Man Jadda Wajada\", ia belajar bahwa kerja keras adalah kunci meraih mimpi.",
                R.drawable.negeri_5_menara,
                false,
                "Motivasi",
                4.7f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Ronggeng Dukuh Paruk",
                "Ahmad Tohari",
                1982,
                "Srintil, seorang ronggeng dari desa terpencil, menjadi simbol tradisi dan perubahan sosial. Novel ini menggambarkan konflik antara budaya lokal dan modernitas.",
                R.drawable.ronggeng_dukuh_paruk,
                false,
                "Sastra",
                4.5f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Supernova: Ksatria, Puteri, dan Bintang Jatuh",
                "Dee Lestari",
                2001,
                "Dhimas dan Reuben, dua mahasiswa di Amerika, menciptakan kisah fiksi ilmiah yang menggambarkan pencarian makna hidup dan cinta. Sebuah novel yang memadukan sains, filsafat, dan romansa.",
                R.drawable.supernova,
                false,
                "Fiksi Ilmiah",
                4.3f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Gadis Kretek",
                "Ratih Kumala",
                2012,
                "Melalui kisah cinta dan industri kretek, novel ini menggambarkan sejarah Indonesia dari masa kolonial hingga modern. Sebuah cerita tentang warisan, identitas, dan perubahan zaman.",
                R.drawable.gadis_kretek,
                false,
                "Sejarah",
                4.6f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Pulang",
                "Leila S. Chudori",
                2012,
                "Dimas Suryo, eksil politik pasca peristiwa 1965, hidup di Paris dengan kenangan dan kerinduan akan tanah air. Novel ini menggambarkan dampak sejarah pada kehidupan pribadi dan keluarga.",
                R.drawable.pulang,
                true,
                "Sejarah",
                4.7f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Saman",
                "Ayu Utami",
                1998,
                "Mengisahkan kehidupan empat perempuan dan seorang mantan pastor bernama Saman. Novel ini mengeksplorasi isu seksualitas, politik, dan kebebasan individu di Indonesia.",
                R.drawable.saman,
                false,
                "Sastra",
                4.1f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Filosofi Kopi",
                "Dee Lestari",
                2006,
                "Kumpulan cerita dan prosa yang menggambarkan kehidupan, cinta, dan makna melalui secangkir kopi. Sebuah refleksi tentang pencarian jati diri dan kebahagiaan.",
                R.drawable.filosofi_kopi,
                false,
                "Fiksi",
                4.4f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Orang-Orang Biasa",
                "Andrea Hirata",
                2019,
                "Sekelompok orang biasa di Kota Belantik merencanakan perampokan demi keadilan. Sebuah kisah penuh humor dan kritik sosial tentang ketimpangan dan harapan.",
                R.drawable.orang_orang_biasa,
                false,
                "Komedi",
                4.0f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Laut Bercerita",
                "Leila S. Chudori",
                2017,
                "Biru Laut, aktivis mahasiswa, menghilang pada masa Orde Baru. Novel ini menggambarkan penderitaan keluarga dan sahabat yang mencari keadilan dan kebenaran.",
                R.drawable.laut_bercerita,
                true,
                "Sejarah",
                4.9f
        ));

        addBookInternal(new Book(idCounter.incrementAndGet(),
                "Hujan Bulan Juni",
                "Sapardi Djoko Damono",
                1994,
                "Kumpulan puisi yang menggambarkan cinta, kesetiaan, dan keindahan alam dengan bahasa yang sederhana namun mendalam. Salah satu karya sastra Indonesia yang paling dikenal dan dicintai.",
                R.drawable.hujan_bulan_juni,
                false,
                "Puisi",
                4.8f
        ));

        // Urutkan buku berdasarkan tahun terbit (terbaru di atas)
        sortBooks();
    }

    // Mengambil semua buku (salinan agar list asli tidak termodifikasi langsung)
    public static List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    // Mengambil buku berdasarkan ID
    public static Book getBookById(long id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    // Menambah buku baru
    public static void addBook(Book book) {
        addBookInternal(book);
        sortBooks(); // Urutkan setelah menambah
    }

    // Helper internal untuk menambah buku
    private static void addBookInternal(Book book) {
        // Pastikan ID unik jika dari form (Uri)
        if (book.getId() == 0) { // Asumsi ID 0 berarti belum diinisialisasi
            long maxId = 0;
            for(Book existingBook : books) {
                if (existingBook.getId() > maxId) {
                    maxId = existingBook.getId();
                }
            }
            books.add(0, new Book(maxId + 1, book.getTitle(), book.getAuthor(), book.getYear(), book.getBlurb(), book.getCoverImageUri(), book.getGenre(), book.getRating()));
        } else {
            books.add(0, book);
        }
    }


    // Mengupdate status 'liked' buku
    public static void updateLikeStatus(long bookId, boolean liked) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setLiked(liked);
        }
    }

    // Mengambil buku yang disukai
    public static List<Book> getFavoriteBooks() {
        List<Book> favoriteBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isLiked()) {
                favoriteBooks.add(book);
            }
        }
        return favoriteBooks;
    }

    // Mengurutkan buku berdasarkan tahun terbit (terbaru di atas)
    private static void sortBooks() {
        books.sort((b1, b2) -> Integer.compare(b2.getYear(), b1.getYear()));
    }

    // Helper untuk mendapatkan ID unik baru (digunakan di AddBookFragment)
    public static long getNewId() {
        return idCounter.incrementAndGet();
    }

    // Mengambil semua genre buku (unik)
    public static List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        for (Book b : books) {
            if (!genres.contains(b.getGenre())) {
                genres.add(b.getGenre());
            }
        }
        Collections.sort(genres);
        return genres;
    }
}