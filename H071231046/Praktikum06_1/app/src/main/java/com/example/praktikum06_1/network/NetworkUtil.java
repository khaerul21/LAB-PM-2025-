package com.example.praktikum06_1.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//Mengecek koneksi internet pada perangkat Android
public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        // Memastikan konteks tidak null
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        // Mendapatkan informasi jaringan aktif
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();// Memeriksa apakah jaringan aktif dan terhubung
    }
}
