package com.example.ig_profile.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.ig_profile.R;

import java.util.ArrayList;
import java.util.List;

public class DetailHighlightActivity extends AppCompatActivity {
    private ImageView imageHighlight;
    private LinearLayout progressContainer;
    private final Handler handler = new Handler();
    private int currentIndex = 0;
    private final List<Integer> imageResList = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();
    private Runnable storyRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_highlight);

        imageHighlight = findViewById(R.id.imageHighlight);
        ImageView imgCover = findViewById(R.id.imgCover);
        TextView tvTitle = findViewById(R.id.tvTitle);
        progressContainer = findViewById(R.id.progressContainer);
        View leftArea = findViewById(R.id.leftArea);
        View rightArea = findViewById(R.id.rightArea);

        // Ambil data dari intent
        String title = getIntent().getStringExtra("title");
        String coverUri = getIntent().getStringExtra("coverUri");
        int coverResId = getIntent().getIntExtra("cover", 0);
        int[] images = getIntent().getIntArrayExtra("images");

        tvTitle.setText(title);

        if (coverUri != null) {
            Glide.with(this).load(coverUri).circleCrop().into(imgCover);
        } else {
            Glide.with(this).load(coverResId).circleCrop().into(imgCover);
        }

        assert images != null;
        for (int resId : images) {
            imageResList.add(resId);
        }

        setupProgressBars();

        leftArea.setOnClickListener(v -> showPrevious());
        rightArea.setOnClickListener(v -> showNext());

        startStory();
    }

    private void setupProgressBars() {
        progressContainer.removeAllViews();
        progressBars.clear();

        for (int i = 0; i < imageResList.size(); i++) {
            ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            if (i < imageResList.size() - 1) {
                params.setMarginEnd(8); // jarak antar bar
            }

            bar.setLayoutParams(params);
            bar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.story_progress));
            bar.setMax(1000);
            bar.setProgress(0);

            progressContainer.addView(bar);
            progressBars.add(bar);
        }
    }

    private void startStory() {
        if (currentIndex >= imageResList.size()) return;
        imageHighlight.setImageResource(imageResList.get(currentIndex));
        animateProgress(currentIndex);
    }

    private void animateProgress(int index) {
        ProgressBar bar = progressBars.get(index);
        bar.setProgress(0);

        storyRunnable = new Runnable() {
            int progress = 0;

            @Override
            public void run() {
                if (progress <= 1000) {
                    bar.setProgress(progress++);
                    handler.postDelayed(this, 15); // 15ms x 1000 = ~15 detik
                } else {
                    currentIndex++;
                    if (currentIndex < imageResList.size()) {
                        startStory();
                    } else {
                        finish();
                    }
                }
            }
        };

        handler.post(storyRunnable);
    }

    private void showNext() {
        if (storyRunnable != null) handler.removeCallbacks(storyRunnable);

        progressBars.get(currentIndex).setProgress(1000); // Selesaikan bar saat ini
        currentIndex++;

        if (currentIndex < imageResList.size()) {
            startStory();
        } else {
            finish(); // Keluar dari highlight jika sudah habis
        }
    }

    private void showPrevious() {
        if (storyRunnable != null) handler.removeCallbacks(storyRunnable);

        if (currentIndex > 0) {
            progressBars.get(currentIndex).setProgress(0); // Reset bar saat ini
            currentIndex--;
            progressBars.get(currentIndex).setProgress(0); // Reset bar sebelumnya juga
            startStory();
        } else {
            // Kalau di awal indeks, reset bar awal
            progressBars.get(currentIndex).setProgress(0);
            startStory();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (storyRunnable != null) handler.removeCallbacks(storyRunnable);
    }
}