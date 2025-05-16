package com.example.praktikum04.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class Book implements Parcelable {
    private String title;
    private String author;
    private String genre;
    private String description;
    private int year;
    private String blurb; // Ringkasan singkat
    private boolean isFav; // Status Like
    private int coverResId = -1;
    private String imageUri;
    private float rating; // rating buku, 0 - 5


    public Book(String title, String author, String genre, int coverResId, String description, String imageUri, int year, String blurb, boolean isFav, float rating) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.coverResId = coverResId;
        this.description = description;
        this.imageUri = imageUri;
        this.year = year;
        this.blurb = blurb;
        this.isFav = isFav;
        this.rating = rating;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        genre = in.readString();
        coverResId = in.readInt();
        description = in.readString();
        year = in.readInt();
        blurb = in.readString();
        isFav = in.readByte() != 0;
        imageUri = in.readString();
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

    public String getImageUri() {
        return imageUri;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    // Getter dan Setter
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getCoverResId() {
        return coverResId;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public String getBlurb() {
        return blurb;
    }

    public boolean isFav() {
        return isFav;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;
        Book other = (Book) obj;
        return Objects.equals(this.title, other.title) &&
                Objects.equals(this.author, other.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }


    public void setfav(boolean favorite) {
        isFav = favorite;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(genre);
        parcel.writeInt(coverResId);
        parcel.writeString(description);
        parcel.writeInt(year);
        parcel.writeString(blurb);
        parcel.writeByte((byte) (isFav ? 1 : 0));
        parcel.writeString(imageUri);
        parcel.writeFloat(rating);
    }


    @Override
    public int describeContents() {
        return 0;
    }
}


