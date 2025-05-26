package com.example.praktikum06_1.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


//Pakai @SerializedName agar Retrofit tahu field JSON mana yang cocok dengan atribut class.
public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("species")
    private String species;

    @SerializedName("gender")
    private String gender;

    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }
}

