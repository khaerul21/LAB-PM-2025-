package com.example.tuprak;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuprak.adapters.GalleryAdapter;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewPostActivity extends AppCompatActivity implements GalleryAdapter.OnImageSelectedListener {

    private static final String TAG = "NewPostActivity";
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final String[] REQUIRED_PERMISSIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ? new String[] { Manifest.permission.READ_MEDIA_IMAGES }
            : new String[] { Manifest.permission.READ_EXTERNAL_STORAGE };

    private ImageView imagePreview;
    private TextView noImageSelectedText;
    private TextView nextButton;
    private RecyclerView galleryRecyclerView;
    private GalleryAdapter galleryAdapter;
    private Uri selectedImageUri;
    private List<Uri> deviceImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        imagePreview = findViewById(R.id.imagePreview);
        noImageSelectedText = findViewById(R.id.noImageSelectedText);
        nextButton = findViewById(R.id.nextButton);
        galleryRecyclerView = findViewById(R.id.galleryRecyclerView);

        findViewById(R.id.closeButton).setOnClickListener(v -> finish());
        findViewById(R.id.cameraButton).setOnClickListener(v -> openCamera());
        findViewById(R.id.multiSelectButton).setOnClickListener(v -> toggleMultiSelect());

        nextButton.setEnabled(false);
        nextButton.setAlpha(0.5f);
        nextButton.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                openCaptionScreen();
            }
        });

        galleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        galleryAdapter = new GalleryAdapter(this, deviceImages, this);
        galleryRecyclerView.setAdapter(galleryAdapter);

        if (hasRequiredPermissions()) {
            loadGalleryImages();
        } else {
            requestPermissions();
        }
    }

    private boolean hasRequiredPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGalleryImages();
            } else {
                Toast.makeText(this, "Permission required to access gallery", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void loadGalleryImages() {
        deviceImages.clear();
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    Uri imageUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    deviceImages.add(imageUri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        galleryAdapter.updateImages(deviceImages);
        if (!deviceImages.isEmpty()) {
            selectImage(deviceImages.get(0));
        }
    }

    @Override
    public void onImageSelected(Uri imageUri) {
        selectImage(imageUri);
    }

    private void selectImage(Uri imageUri) {
        selectedImageUri = imageUri;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            imagePreview.setImageBitmap(bitmap);
            noImageSelectedText.setVisibility(View.GONE);
            nextButton.setEnabled(true);
            nextButton.setAlpha(1.0f);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        Toast.makeText(this, "Camera feature will be implemented", Toast.LENGTH_SHORT).show();
    }

    private void toggleMultiSelect() {
        Toast.makeText(this, "Multi-select feature will be implemented", Toast.LENGTH_SHORT).show();
    }

    private void openCaptionScreen() {
        startCrop(selectedImageUri);
    }

    private void startCrop(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), 
                "cropped_" + System.currentTimeMillis() + ".jpg"));
        
        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1080, 1080)
                .withOptions(getCropOptions());
        
        uCrop.start(this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(getResources().getColor(R.color.black, getTheme()));
        options.setToolbarColor(getResources().getColor(R.color.black, getTheme()));
        options.setToolbarWidgetColor(getResources().getColor(R.color.white, getTheme()));
        options.setActiveControlsWidgetColor(getResources().getColor(R.color.instagram_blue, getTheme()));
        options.setFreeStyleCropEnabled(false);
        options.setShowCropGrid(true);
        return options;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedImageUri = UCrop.getOutput(data);
            Intent intent = new Intent(this, CaptionActivity.class);
            intent.putExtra("imageUri", croppedImageUri.toString());
            startActivity(intent);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable error = UCrop.getError(data);
            Toast.makeText(this, "Image cropping failed: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }
}