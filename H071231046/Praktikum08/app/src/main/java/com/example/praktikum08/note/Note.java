package com.example.praktikum08.note;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Note implements Parcelable {
    private int id;
    private String judul, deskripsi;
    private String tanggalWaktu;
    public Note() {
    }

    public Note(int id, String judul, String deskripsi, String tanggalWaktu) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggalWaktu = tanggalWaktu;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        judul = in.readString();
        deskripsi = in.readString();
        tanggalWaktu = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(judul);
        dest.writeString(deskripsi);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggalWaktu() {
        return tanggalWaktu;
    }

    public void setTanggalWaktu(String tanggalWaktu) {
        this.tanggalWaktu = tanggalWaktu;
    }
}

