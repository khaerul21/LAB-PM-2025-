package com.example.tuprak;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class StoryViewerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final long STORY_DURATION = 5000;
    private static final float SWIPE_THRESHOLD = 100;
    private static final float SWIPE_VELOCITY_THRESHOLD = 100;

    private ImageView storyImage;
    private TextView usernameText;
    private TextView timestampText;
    private ImageView closeButton;
    private ProgressBar storyProgress;
    
    private GestureDetectorCompat gestureDetector;
    private ValueAnimator progressAnimator;
    private Handler storyHandler;
    private Runnable storyRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_viewer);

        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        storyImage = findViewById(R.id.storyImage);
        usernameText = findViewById(R.id.storyUsername);
        timestampText = findViewById(R.id.storyTimestamp);
        closeButton = findViewById(R.id.closeButton);
        storyProgress = findViewById(R.id.storyProgress);

        gestureDetector = new GestureDetectorCompat(this, this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int imageResId = intent.getIntExtra("image_resource", R.drawable.ic_launcher_background);
        String timestamp = intent.getStringExtra("timestamp");

        int storyPosition = intent.getIntExtra("story_position", -1);
        
        if (storyPosition != -1) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("viewed_story_position", storyPosition);
            setResult(RESULT_OK, resultIntent);
        }

        usernameText.setText(title);
        storyImage.setImageResource(imageResId);
        timestampText.setText(timestamp != null ? timestamp : "2 HOURS AGO");

        closeButton.setOnClickListener(v -> finish());

        startProgressAnimation();
        
        storyHandler = new Handler();
        storyRunnable = this::finish;
        storyHandler.postDelayed(storyRunnable, STORY_DURATION);
    }

    private void startProgressAnimation() {
        progressAnimator = ValueAnimator.ofInt(0, 100);
        progressAnimator.setDuration(STORY_DURATION);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            storyProgress.setProgress(value);
        });
        progressAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();
        
        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD && diffY > 0) {
            finish();
            return true;
        }
        return false;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        if (storyHandler != null && storyRunnable != null) {
            storyHandler.removeCallbacks(storyRunnable);
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_down);
    }
}