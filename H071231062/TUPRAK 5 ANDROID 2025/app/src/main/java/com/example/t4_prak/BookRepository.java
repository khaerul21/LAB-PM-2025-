package com.example.t4_prak;

import android.content.Context;

import com.example.t4_prak.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private final List<Book> books;

    private static final String FILE_NAME = "books_data.dat";

    private BookRepository() {
        books = new ArrayList<>();
        books.add(new Book("The Shining", "Stephen King", 1977,
                "Jack Torrance membawa keluarganya ke hotel terpencil yang angker, lalu perlahan kehilangan akal sehat.",
                R.drawable.cover_theshining, false, "Horror", 4.5f));

        books.add(new Book("IT", "Stephen King", 1986,
                "Sekelompok anak-anak menghadapi makhluk jahat dalam wujud badut bernama Pennywise.",
                R.drawable.cover_it, false, "Horror", 4.7f));

        books.add(new Book("Bird Box", "Josh Malerman", 2014,
                "Dunia diselimuti kengerian ketika entitas misterius membuat siapa pun yang melihatnya menjadi gila.",
                R.drawable.cover_bird_box, false, "Thriller", 4.3f));

        books.add(new Book("The Haunting of Hill House", "Shirley Jackson", 1959,
                "Eksperimen supranatural berubah menjadi teror nyata dalam rumah berhantu klasik ini.",
                R.drawable.cover_hill_house, false, "Horror", 4.1f));

        books.add(new Book("Hell House", "Richard Matheson", 1971,
                "Tim ilmuwan menghadapi kekuatan gaib luar biasa di rumah paling berhantu yang pernah ada.",
                R.drawable.cover_hell_house, false, "Horror", 4.2f));

        books.add(new Book("The Bourne Identity", "Robert Ludlum", 1980,
                "Pria dengan amnesia mencoba mengungkap identitasnya sambil diburu oleh pembunuh bayaran.",
                R.drawable.cover_bourne, false, "Action", 4.6f));

        books.add(new Book("Jack Reacher: One Shot", "Lee Child", 2005,
                "Jack Reacher mengungkap konspirasi di balik penembakan massal yang tampak sederhana.",
                R.drawable.cover_reacher, false, "Mystery", 4.4f));

        books.add(new Book("The Hunger Games", "Suzanne Collins", 2008,
                "Katniss Everdeen bertarung dalam kompetisi mematikan demi bertahan hidup dan membela keadilan.",
                R.drawable.cover_hunger_games, false, "Dystopian", 4.8f));

        books.add(new Book("Shooter (Point of Impact)", "Stephen Hunter", 1993,
                "Sniper veteran dijebak dan harus mengungkap kebenaran demi membersihkan namanya.",
                R.drawable.cover_shooter, false, "Thriller", 4.5f));

        books.add(new Book("Alex Rider: Stormbreaker", "Anthony Horowitz", 2000,
                "Remaja yatim piatu direkrut sebagai mata-mata untuk menyusup ke organisasi teknologi berbahaya.",
                R.drawable.cover_stormbreaker, false, "Adventure", 4.3f));

        books.add(new Book("Dune", "Frank Herbert", 1965,
                "Paul Atreides bertarung demi kehormatan keluarganya dan masa depan planet gurun Arrakis.",
                R.drawable.cover_dune, false, "Science Fiction", 4.7f));

        books.add(new Book("The Witcher: Blood of Elves", "Andrzej Sapkowski", 1994,
                "Geralt melindungi seorang anak perempuan istimewa yang menentukan nasib dunia.",
                R.drawable.cover_witcher, false, "Fantasy", 4.6f));

        books.add(new Book("Ender's Game", "Orson Scott Card", 1985,
                "Ender Wiggin dilatih untuk memimpin manusia dalam perang melawan alien.",
                R.drawable.cover_enders_game, false, "Science Fiction", 4.8f));

        books.add(new Book("Eragon", "Christopher Paolini", 2002,
                "Remaja menemukan telur naga dan menjadi pahlawan dalam perang melawan kekaisaran jahat.",
                R.drawable.cover_eragon, false, "Fantasy", 4.5f));

        books.add(new Book("The Maze Runner", "James Dashner", 2009,
                "Sekelompok remaja harus melarikan diri dari labirin mematikan dan mengungkap siapa mereka sebenarnya.",
                R.drawable.cover_maze_runner, false, "Dystopian", 4.3f));
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Book> getLikedBooks() {
        List<Book> likedBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isLiked()) {
                likedBooks.add(book);
            }
        }
        return likedBooks;
    }

    public void saveData(Context context) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(Context context) {
        try (ObjectInputStream ois = new ObjectInputStream(
                context.openFileInput(FILE_NAME))) {
            List<Book> loadedBooks = (List<Book>) ois.readObject();
            books.clear();
            books.addAll(loadedBooks);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}