package com.example.networking;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("info")
    private Info info;
    @SerializedName("results")
    private List<Character> results;

    public Info getInfo() { return info; }
    public List<Character> getResults() { return results; }
}