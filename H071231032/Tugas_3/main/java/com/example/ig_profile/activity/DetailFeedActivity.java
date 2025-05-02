package com.example.ig_profile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ig_profile.adapter.FeedAdapter;
import com.example.ig_profile.R;
import com.example.ig_profile.models.FeedItem;
import com.example.ig_profile.models.User;

import java.util.List;
import java.util.Objects;

public class DetailFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        RecyclerView detailFeedRecyclerView = findViewById(R.id.detailRecyclerView);
        detailFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        User currentUser = getIntent().getParcelableExtra("user", User.class);

        assert currentUser != null;
        List<FeedItem> feedItems = currentUser.getFeedItems();
        ImageButton btnBack = findViewById(R.id.back_to_profile);

        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        FeedAdapter adapter = new FeedAdapter(feedItems, currentUser, this, false);
        detailFeedRecyclerView.setAdapter(adapter);

        int position = getIntent().getIntExtra("feedPosition", 0);
        detailFeedRecyclerView.post(() -> {
            detailFeedRecyclerView.scrollToPosition(position);
            detailFeedRecyclerView.postDelayed(() -> {
                View targetView = Objects.requireNonNull(detailFeedRecyclerView.getLayoutManager()).findViewByPosition(position);
                if (targetView != null) {
                    int y = targetView.getTop() - (detailFeedRecyclerView.getHeight() / 2) + (targetView.getHeight() / 2);
                    detailFeedRecyclerView.smoothScrollBy(0, y);
                }
            }, 100);
        });

    }
}