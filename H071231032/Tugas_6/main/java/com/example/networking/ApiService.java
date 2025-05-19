package com.example.networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @GET("character")
    Call<ApiResponse> getCharacters(@Query("page") int page);

    @GET("character/{id}")
    Call<Character> getCharacterDetail(@Path("id") int id);

    @GET
    Call<ApiResponse> getCharactersFromUrl(@Url String url);
}
