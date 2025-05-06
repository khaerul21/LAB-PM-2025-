package com.example.praktikum03.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.praktikum03.R;
import com.example.praktikum03.models.Story;
import com.example.praktikum03.utils.DataSource;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryActivity extends AppCompatActivity {
    private ImageView imageViewStory, imageViewClose;
    private CircleImageView imageViewProfile;
    private TextView textViewStoryTitle;
    private int currentImageIndex = 0;
    private LinearLayout storyProgressContainer;
    private List<View> progressBars = new ArrayList<>();


    private Story story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyProgressContainer = findViewById(R.id.storyProgressContainer);
        imageViewStory = findViewById(R.id.imageViewStory);
        imageViewClose = findViewById(R.id.imageViewClose);
        imageViewProfile = findViewById(R.id.iv_profile);
        textViewStoryTitle = findViewById(R.id.textViewStoryTitle);

        int storyId = getIntent().getIntExtra("STORY_ID", 0);
        if (storyId != 0) {
            for (Story s : DataSource.getStoryList()) {
                if (s.getId() == storyId) {
                    story = s;
                    break;
                }
            }

            if (story != null) {
                setupStoryDetail();
            }
        }

        imageViewClose.setOnClickListener(v -> finish());
    }

    private void setupProgressBars(int totalSlides) {
        storyProgressContainer.removeAllViews();
        progressBars.clear();

        for (int i = 0; i < totalSlides; i++) {
            View bar = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.setMargins(4, 0, 4, 0);
            bar.setLayoutParams(params);
            bar.setBackgroundColor(getResources().getColor(R.color.progressInactive)); // warna default
            progressBars.add(bar);
            storyProgressContainer.addView(bar);
        }
    }

    //    private void setupStoryDetail() {
//        imageViewStory.setImageResource(story.getImageResourceIds());
//
//        imageViewProfile.setImageResource(DataSource.getCurrentUser().getProfileImageResourceId());
//
//        textViewStoryTitle.setText(story.getTitle());
//    }
    private void setupStoryDetail() {
        List<Integer> images = story.getImageResourceIds();
        currentImageIndex = 0; // Reset posisi ke gambar pertama

        if (images != null && !images.isEmpty()) {
            imageViewStory.setImageResource(images.get(currentImageIndex));

            // Set progress bar berdasarkan jumlah gambar
            setupProgressBars(images.size());
            updateProgressBar(currentImageIndex);

            imageViewStory.setOnClickListener(v -> {
                currentImageIndex++;
                if (currentImageIndex >= images.size()) {
                    finish(); // Keluar dari Story setelah gambar terakhir
                } else {
                    imageViewStory.setImageResource(images.get(currentImageIndex));
                    updateProgressBar(currentImageIndex); //spy pb-nya berpindah
                }
            });
        }

        imageViewProfile.setImageResource(DataSource.getCurrentUser().getProfileImageResourceId());
        textViewStoryTitle.setText(story.getTitle());

    }
    private void updateProgressBar(int index) {
        for (int i = 0; i < progressBars.size(); i++) {
            if (i <= index) {
                progressBars.get(i).setBackgroundColor(getResources().getColor(R.color.progressActive));
            } else {
                progressBars.get(i).setBackgroundColor(getResources().getColor(R.color.progressInactive));
            }
        }
    }


}