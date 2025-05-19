package com.example.networking;

import com.google.gson.annotations.SerializedName;

public class Info {
    @SerializedName("next")
    private String next;

    public String getNext() { return next; }
}