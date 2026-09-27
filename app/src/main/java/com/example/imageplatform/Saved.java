package com.example.imageplatform;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.imageplatform.adapters.ImageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Saved extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Image> list;
    BottomNavigationView bottomNav;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        userId = getIntent().getIntExtra("userId", -1);

        recyclerView = findViewById(R.id.recyclerView);
        bottomNav = findViewById(R.id.bottom_navigation);

        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        list = new ArrayList<>();

        Set<String> savedData = SaveManager.getAll(this);

        for (String data : savedData) {
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

        recyclerView.setAdapter(new ImageAdapter(this, list, userId));

        BottomNavHelper.setup(this, bottomNav, userId);
        bottomNav.setSelectedItemId(R.id.navsaved);
    }
}