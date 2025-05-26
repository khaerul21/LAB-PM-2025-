package com.example.praktikum06_1.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {

    //API /character mengembalikan JSON dengan key "results", berisi array karakter.
    @SerializedName("results")
    private List<User> results;

    public List<User> getResults() {
        return results;
    }
}
