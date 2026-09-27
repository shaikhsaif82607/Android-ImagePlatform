package com.example.imageplatform;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.imageplatform.adapters.ImageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Profile extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnUploads, btnSaved, logoutBtn;
    TextView postCount, savedCount, userEmail;

    List<Image> list;
    ImageAdapter adapter;

    BottomNavigationView bottomNav;

    int userId;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ✅ GET USER ID
        userId = getIntent().getIntExtra("userId", -1);

        // 🔍 INIT
        recyclerView = findViewById(R.id.recyclerView);
        btnUploads = findViewById(R.id.btnUploads);
        btnSaved = findViewById(R.id.btnSaved);
        logoutBtn = findViewById(R.id.logoutBtn);

        postCount = findViewById(R.id.postCount);
        savedCount = findViewById(R.id.savedCount);
        userEmail = findViewById(R.id.userName);

        bottomNav = findViewById(R.id.bottom_navigation);

        db = new DBHelper(this);

        // ✅ SET EMAIL FROM DB
        String email = db.getUserEmail(userId);
        userEmail.setText(email != null ? email : "User");

        // 📱 GRID
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        // 📦 LIST + ADAPTER
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list, userId, true); // default uploads mode
        recyclerView.setAdapter(adapter);

        // 🔥 DEFAULT LOAD
        loadUploads();
        highlight(true);

        // 🔘 BUTTONS
        btnUploads.setOnClickListener(v -> {
            loadUploads();
            highlight(true);
        });

        btnSaved.setOnClickListener(v -> {
            loadSaved();
            highlight(false);
        });

        // 🔥 LOGOUT
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // 🔻 NAVIGATION
        BottomNavHelper.setup(this, bottomNav, userId);
        bottomNav.setSelectedItemId(R.id.navprofile);
    }

    // ✅ LOAD USER POSTS
    private void loadUploads() {
        list.clear();

        List<Image> userPosts = db.getUserPosts(userId);
        list.addAll(userPosts);

        postCount.setText(list.size() + " Posts");

        adapter.setProfileMode(true); // 🔥 show delete
        adapter.notifyDataSetChanged();
    }

    // 🔥 LOAD SAVED POSTS
    private void loadSaved() {
        list.clear();

        Set<String> saved = SaveManager.getAll(this);
        savedCount.setText(saved.size() + " Saved");

        for (String data : saved) {
            try {
                if (data.startsWith("content://")) {
                    list.add(new Image("Saved", data));
                } else {
                    list.add(new Image("Saved", Integer.parseInt(data)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter.setProfileMode(false); // 🔥 hide delete
        adapter.notifyDataSetChanged();
    }

    // 🎯 UI EFFECT
    private void highlight(boolean uploads) {
        if (uploads) {
            btnUploads.setAlpha(1f);
            btnSaved.setAlpha(0.5f);
        } else {
            btnUploads.setAlpha(0.5f);
            btnSaved.setAlpha(1f);
        }
    }
}