package com.example.imageplatform;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imageplatform.adapters.ImageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Detail extends AppCompatActivity {

    ImageView detailImage, likeBtn;
    TextView title, likeCount;
    MaterialButton saveBtn, followBtn;
    RecyclerView moreRecycler;
    BottomNavigationView bottomNav;

    boolean liked = false;
    int likes = 150;

    String imageData;

    int userId; // ✅ ADD THIS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // ✅ GET USER ID
        userId = getIntent().getIntExtra("userId", -1);

        detailImage = findViewById(R.id.detailImage);
        title = findViewById(R.id.title);
        likeBtn = findViewById(R.id.likeBtn);
        likeCount = findViewById(R.id.likeCount);
        saveBtn = findViewById(R.id.saveBtn);
        followBtn = findViewById(R.id.followBtn);
        moreRecycler = findViewById(R.id.moreRecycler);
        bottomNav = findViewById(R.id.bottom_navigation);

        String t = getIntent().getStringExtra("title");
        int imageRes = getIntent().getIntExtra("image", -1);
        String imageUri = getIntent().getStringExtra("imageUri");

        title.setText(t);

        imageData = (imageUri != null) ? imageUri : String.valueOf(imageRes);

        try {
            if (imageUri != null && !imageUri.isEmpty()) {
                detailImage.setImageURI(Uri.parse(imageUri));
            } else if (imageRes != -1) {
                detailImage.setImageResource(imageRes);
            } else {
                detailImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } catch (Exception e) {
            detailImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        likeCount.setText(String.valueOf(likes));

        likeBtn.setOnClickListener(v -> {
            liked = !liked;

            if (liked) {
                likes++;
                likeBtn.setImageResource(R.drawable.filledheart);
            } else {
                likes--;
                likeBtn.setImageResource(R.drawable.heart);
            }

            likeCount.setText(String.valueOf(likes));
        });

        if (SaveManager.isSaved(this, imageData)) {
            saveBtn.setText("Saved");
        }

        saveBtn.setOnClickListener(v -> {
            if (SaveManager.isSaved(this, imageData)) {
                SaveManager.remove(this, imageData);
                saveBtn.setText("Save");
            } else {
                SaveManager.save(this, imageData);
                saveBtn.setText("Saved");
            }
        });

        followBtn.setOnClickListener(v ->
                Toast.makeText(this, "Followed!", Toast.LENGTH_SHORT).show()
        );

        moreRecycler.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        List<Image> moreList = new ArrayList<>();
        moreList.add(new Image("More 1", R.drawable.download__12_));
        moreList.add(new Image("More 2", R.drawable.download__23_));
        moreList.add(new Image("More 3", R.drawable._373371));

        // ✅ FINAL FIX
        ImageAdapter adapter = new ImageAdapter(this, moreList, userId);
        moreRecycler.setAdapter(adapter);
        // ✅ FIXED NAVIGATION
        BottomNavHelper.setup(this, bottomNav, userId);
    }
}