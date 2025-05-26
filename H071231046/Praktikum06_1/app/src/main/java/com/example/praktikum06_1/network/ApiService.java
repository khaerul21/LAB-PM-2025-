package com.example.praktikum06_1.network;

import com.example.praktikum06_1.response.User;
import com.example.praktikum06_1.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("character")
    // Menggunakan anotasi @Query untuk menambahkan parameter query 'page'
    Call<UserResponse> getCharacterData(@Query("page") int page);

    //endpoint untuk ambil detail satu karakter.
    @GET("character/{id}")
    // Menggunakan anotasi @Path untuk menggantikan {id} dengan nilai yang diberikan
    Call<User> getCharacterById(@Path("id") int id);
}
