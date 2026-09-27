package com.example.imageplatform;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.imageplatform.adapters.ImageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Image> list;
    BottomNavigationView bottomNav;
    ImageAdapter adapter;

    CardView searchBar;

    DBHelper db;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userId = getIntent().getIntExtra("userId", -1);

        recyclerView = findViewById(R.id.recyclerView);
        bottomNav = findViewById(R.id.bottom_navigation);

        // ✅ CONNECT SEARCH BAR
        searchBar = findViewById(R.id.search_bar);

        db = new DBHelper(this);

        // ✅ SEARCH CLICK
        searchBar.setOnClickListener(v -> {

            Intent intent = new Intent(Home.this, Search.class);
            intent.putExtra("userId", userId);

            startActivity(intent);
        });

        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL
                )
        );

        // ✅ LOAD POSTS
        list = db.getPosts();

        // ✅ FALLBACK IMAGES
        if (list == null || list.size() == 0) {

            list = new ArrayList<>();

            list.add(new Image(
                    "Onizuka",
                    R.drawable.download__12_
            ));

            list.add(new Image(
                    "Africa Baby",
                    R.drawable.no_more_heroes_official_uaa_rankers_bible
            ));
        }

        adapter = new ImageAdapter(this, list, userId);

        recyclerView.setAdapter(adapter);

        // ✅ BOTTOM NAV
        BottomNavHelper.setup(this, bottomNav, userId);

        bottomNav.setSelectedItemId(R.id.navhome);
    }
}