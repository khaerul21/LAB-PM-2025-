package com.example.t6_prak.network;

import com.example.t6_prak.model.CharacterResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/character")
    Call<CharacterResponse> getCharacters(@Query("page") int page);
}
