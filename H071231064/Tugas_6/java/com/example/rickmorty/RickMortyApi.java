package com.example.rickmorty;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RickMortyApi {

    class Info {
        public int count;
        public int pages;
        public String next;
        public String prev;
    }

    class Character {
        public int id;
        public String name;
        public String status;
        public String species;
        public String type;
        public String gender;
        public Origin origin;
        public Location location;
        public String image;

        public static class Origin {
            public String name;
            public String url;
        }

        public static class Location {
            public String name;
            public String url;
        }
    }

    class CharacterResponse {
        public Info info;
        public List<Character> results;
    }

    @GET("character")
    Call<CharacterResponse> getCharacters(@Query("page") int page);

    @GET("character/{id}")
    Call<Character> getCharacter(@Path("id") int id);
}
