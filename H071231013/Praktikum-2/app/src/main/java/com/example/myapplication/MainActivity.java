package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final int REQUEST_EDIT_PROFILE = 1001;

    private TextView profileTitle, usernameView, tvBio;
    private CircleImageView profileImageView;
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private FloatingActionButton fabNewPost;

    private String currentName     = "John Doe";
    private String currentUsername = "john_doe";
    private String currentBio      = "";
    private String currentPhotoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind UI
        profileTitle     = findViewById(R.id.profile_title);
        usernameView     = findViewById(R.id.username);
        tvBio            = findViewById(R.id.tv_bio);
        profileImageView = findViewById(R.id.profile_image_large);
        fabNewPost       = findViewById(R.id.fab_new_post);

        // RecyclerView setup (optional)
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(postAdapter);
        addSamplePosts();

        // default image
        profileImageView.setImageResource(R.drawable.p2);

        // load from prefs & tampilkan
        loadProfileData();
        updateProfileDisplay();

        // klik Edit Profil
        findViewById(R.id.edit_profile_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("name", currentName);
            intent.putExtra("username", currentUsername);
            intent.putExtra("bio", currentBio);
            if (currentPhotoUri != null) {
                intent.putExtra("photoUri", currentPhotoUri);
                Log.d(TAG, "Sending photo URI: " + currentPhotoUri);
            }
            startActivityForResult(intent, REQUEST_EDIT_PROFILE);
        });
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentName     = prefs.getString("name", currentName);
        currentUsername = prefs.getString("username", currentUsername);
        currentBio      = prefs.getString("bio", currentBio);
        // photoUri hanya session
    }

    private void updateProfileDisplay() {
        profileTitle.setText(currentName);
        usernameView.setText(currentUsername);
        tvBio.setText(currentBio);
        if (currentPhotoUri != null) {
            try {
                profileImageView.setImageURI(Uri.parse(currentPhotoUri));
            } catch (Exception e) {
                Log.e(TAG, "Error loading image URI", e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK && data != null) {
            currentName = data.getStringExtra("name");
            currentUsername = data.getStringExtra("username");
            currentBio = data.getStringExtra("bio");
            String photoUri = data.getStringExtra("photoUri");
            if (photoUri != null) currentPhotoUri = photoUri;

            // simpan ke prefs
            SharedPreferences.Editor editor =
                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("name", currentName);
            editor.putString("username", currentUsername);
            editor.putString("bio", currentBio);
            editor.apply();

            updateProfileDisplay();
        }
    }

    private void addSamplePosts() {
        postList.add(new Post("seblak bukan makanan manusia", 247, 499, 2600));
        postList.add(new Post("kangen ayam geprek", 43, 140, 139));
        postList.add(new Post("kangen es teh + mie ayam", 17, 29, 112));
        postList.add(new Post("kangen stv", 0, 0, 0));
        postAdapter.notifyDataSetChanged();
    }
}
