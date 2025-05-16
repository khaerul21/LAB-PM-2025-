package com.example.utils;

import com.example.praktikum04.R;
import com.example.praktikum04.models.Book;

import java.util.ArrayList;

public class BookDummy {
    public static ArrayList<Book> bookList = new ArrayList<>();
    public static ArrayList<Book> FavBooks = new ArrayList<>();


    static {
        bookList.add(new Book(
                "Berani Tidak Disukai",
                "Ichiro Kishimi and Fumitake Koga",
                "Self-improvement",
                R.drawable.cv_beranitidakdisukai,
                "Membaca buku ini bisa mengubah hidup anda. Jutaan orang sudah menarik manfaat darinya, dan sekarang adalah giliran anda. Berani Tidak Disukai, yang sudah terjual lebih dari 3,5 juta eksemplar, mengungkap rahasia mengeluarkan kekuatan terpendam yang memungkinkan Anda meraih kebahagiaan yang hakiki dan menjadi sosok yang Anda idam-idamkan. Apakah kebahagiaan adalah sesuatu yang Anda pilih?",
                null,
                2018,
                "Membaca buku ini bisa mengubah hidup anda. Jutaan orang sudah menarik manfaat darinya. Sekarang giliran anda",
                false,
                (float)(1 + Math.random() * 4)
        ));
        bookList.add(new Book("Atomic Habits", "James Clear", "Self-Help, Psychology",
                R.drawable.cv_atomichabits, "Panduan mengubah kebiasaan kecil menjadi perubahan besar.",
                null, 2018, "Perubahan kecil menciptakan hasil luar biasa.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy, Adventure",
                R.drawable.cv_thehobbit, "Perjalanan epik Bilbo Baggins ke Gunung Sunyi.",
                null, 1937, "Awal dari dunia Middle-Earth.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Sapiens", "Yuval Noah Harari", "History, Science",
                R.drawable.cv_sapiens, "Evolusi manusia dari primata ke penguasa dunia.",
                null, 2011, "Sejarah umat manusia yang menggugah pikiran.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Laskar Pelangi", "Andrea Hirata", "Drama, Inspiratif",
                R.drawable.cv_laskarpelangi, "Kisah perjuangan anak-anak miskin di Belitung.",
                null, 2005, "Sebuah ode untuk pendidikan dan harapan.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Dilan 1990", "Pidi Baiq", "Romance, Teen",
                R.drawable.cv_dilan1990, "Cinta remaja Bandung antara Dilan dan Milea.",
                null, 2014, "Cinta lucu, nyeleneh, tapi mengharukan.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Rich Dad Poor Dad", "Robert T. Kiyosaki", "Finance, Self-Help",
                R.drawable.cv_richdadpoordad, "Pelajaran finansial dari dua sosok ayah.",
                null, 1997, "Cara berpikir orang kaya yang berbeda.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("To Kill a Mockingbird", "Harper Lee", "Classic, Legal",
                R.drawable.cv_tokillamockingbird, "Ketidakadilan rasial di Amerika Selatan.",
                null, 1960, "Melawan prasangka melalui mata anak-anak.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Filosofi Teras", "Henry Manampiring", "Self-Help, Filosofi",
                R.drawable.cv_filosofiteras, "Stoisisme dalam hidup sehari-hari orang Indonesia.",
                null, 2018, "Ketenangan batin lewat logika dan kontrol diri.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "Fantasy, Magic",
                R.drawable.cv_harrypoter, "Petualangan penyihir muda di Hogwarts.",
                null, 1997, "Awal dari dunia sihir penuh misteri.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("The Psychology of Money", "Morgan Housel", "Finance, Psychology",
                R.drawable.cv_thepsychologyofmoney, "Bagaimana emosi memengaruhi keputusan finansial.",
                null, 2020, "Uang bukan hanya angka, tapi juga perilaku.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("The Subtle Art of Not Giving a F*ck", "Mark Manson", "Self-Help, Humor",
                R.drawable.cv_thesubtleart, "Jalan hidup anti-motivasi klise.",
                null, 2016, "Belajar untuk menerima ketidaksempurnaan hidup.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Negeri 5 Menara", "Ahmad Fuadi", "Inspiratif, Remaja",
                R.drawable.cv_negeri5menara, "Kisah santri di pesantren penuh cita-cita.",
                null, 2009, "Man Jadda Wajada: siapa bersungguh-sungguh, dia berhasil.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Percy Jackson: The Lightning Thief", "Rick Riordan", "Fantasy, Mythology",
                R.drawable.cv_thelightningthief, "Dewa Yunani di dunia modern dan remaja setengah dewa.",
                null, 2005, "Petualangan remaja dan mitologi bergabung.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("Bumi", "Tere Liye", "Fiksi, Petualangan",
                R.drawable.cv_bumi, "Rahasia dunia paralel yang menanti untuk dijelajahi.",
                null, 2014, "Bumi menyimpan kekuatan dan pertarungan besar.", false, (float)(1 + Math.random() * 4)));

        bookList.add(new Book("1984", "George Orwell", "Dystopia, Political",
                R.drawable.cv_1984, "Dunia di bawah pengawasan total Big Brother.",
                null, 1949, "Kebebasan, kebenaran, dan manipulasi kekuasaan.", false, (float)(1 + Math.random() * 4)));
    }};