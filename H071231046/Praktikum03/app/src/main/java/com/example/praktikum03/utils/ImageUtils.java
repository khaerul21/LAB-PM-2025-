package com.example.praktikum03.utils;

import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.Map;
import android.util.SparseArray;

public class ImageUtils {
    private static final Map<Integer, Bitmap> uploadedImages = new HashMap<>();

    public static void addUploadedImage(int postId, Bitmap bitmap) {
        uploadedImages.put(postId, bitmap);
    }

    public static Bitmap getUploadedImage(int postId) {
        return uploadedImages.get(postId);
    }

    public static boolean hasUploadedImage(int postId) {
        return uploadedImages.containsKey(postId);
    }
}
