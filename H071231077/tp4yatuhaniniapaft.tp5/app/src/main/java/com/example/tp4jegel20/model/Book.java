    package com.example.tp4jegel20.model;

    import android.os.Parcel;
    import android.os.Parcelable;

    import androidx.annotation.NonNull;

    import java.util.Objects;

    public class Book implements Parcelable {
        private String title;
        private String author;
        private int yearPublished;
        private String blurb;
        private String coverImageUrl;
        private boolean isFavorite;
        private String genre;
        private float rating;
        private long createdAt;

        public Book(String title, String author, int yearPublished, String blurb,
                    String coverImageUrl, String genre) {
            this.title = title;
            this.author = author;
            this.yearPublished = yearPublished;
            this.blurb = blurb;
            this.coverImageUrl = coverImageUrl;
            this.isFavorite = false;
            this.genre = genre;
            this.rating = 0.0f;
            this.createdAt = System.currentTimeMillis();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getYearPublished() {
            return yearPublished;
        }

        public void setYearPublished(int yearPublished) {
            this.yearPublished = yearPublished;
        }

        public String getBlurb() {
            return blurb;
        }

        public void setBlurb(String blurb) {
            this.blurb = blurb;
        }

        public String getCoverImageUrl() {
            return coverImageUrl;
        }

        public void setCoverImageUrl(String coverImageUrl) {
            this.coverImageUrl = coverImageUrl;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        // Parcelable implementation
        protected Book(Parcel in) {
            title = in.readString();
            author = in.readString();
            yearPublished = in.readInt();
            blurb = in.readString();
            coverImageUrl = in.readString();
            isFavorite = in.readByte() != 0;
            genre = in.readString();
            rating = in.readFloat();
            createdAt = in.readLong();
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(author);
            dest.writeInt(yearPublished);
            dest.writeString(blurb);
            dest.writeString(coverImageUrl);
            dest.writeByte((byte) (isFavorite ? 1 : 0));
            dest.writeString(genre);
            dest.writeFloat(rating);
            dest.writeLong(createdAt);
        }

        @Override
        public int describeContents() {
            return 0;
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

        // Equals and HashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return title.equals(book.title) &&
                    author.equals(book.author);
        }

        @Override
        public int hashCode() {
            // hashCode juga harus sesuai dengan equals
            return Objects.hash(title, author);
        }

        @NonNull
        @Override
        public String toString() {
            return "Book{" +
                    "title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", yearPublished=" + yearPublished +
                    ", genre='" + genre + '\'' +
                    ", isFavorite=" + isFavorite +
                    '}';
        }
    }