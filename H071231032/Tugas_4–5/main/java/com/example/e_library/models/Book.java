package com.example.e_library.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private long id;
    private String title;
    private String author;
    private int year;
    private String blurb;
    private Uri coverImageUri; // Untuk gambar dari galeri
    private int coverImageResId; // Untuk gambar dummy dari drawable
    private boolean liked;
    private String genre;
    private float rating;

    // Constructor untuk buku baru dari form (dengan Uri)
    public Book(long id, String title, String author, int year, String blurb, Uri coverImageUri, String genre, float rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.blurb = blurb;
        this.coverImageUri = coverImageUri;
        this.coverImageResId = 0;
        this.liked = false;
        this.genre = genre;
        this.rating = rating;
    }

    // Constructor untuk buku dummy (dengan ResId)
    public Book(long id, String title, String author, int year, String blurb, int coverImageResId, boolean liked, String genre, float rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.blurb = blurb;
        this.coverImageUri = null; // Tidak pakai Uri
        this.coverImageResId = coverImageResId;
        this.liked = liked;
        this.genre = genre;
        this.rating = rating;
    }

    // --- Parcelable Implementation ---
    protected Book(Parcel in) {
        id = in.readLong();
        title = in.readString();
        author = in.readString();
        year = in.readInt();
        blurb = in.readString();
        coverImageUri = in.readParcelable(Uri.class.getClassLoader());
        coverImageResId = in.readInt();
        liked = in.readByte() != 0;
        genre = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(year);
        dest.writeString(blurb);
        dest.writeParcelable(coverImageUri, flags);
        dest.writeInt(coverImageResId);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeString(genre);
        dest.writeFloat(rating);
    }

    // --- Getters & Setters ---
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getBlurb() { return blurb; }
    public Uri getCoverImageUri() { return coverImageUri; }
    public int getCoverImageResId() { return coverImageResId; }
    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }
    public String getGenre() { return genre; }
    public float getRating() { return rating; }

}