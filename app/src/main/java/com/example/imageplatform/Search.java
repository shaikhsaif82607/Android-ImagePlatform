package com.example.imageplatform;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.imageplatform.adapters.ImageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText searchInput;
    BottomNavigationView bottomNav;

    List<Image> fullList;
    List<Image> filteredList;

    ImageAdapter adapter;

    DBHelper db;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userId = getIntent().getIntExtra("userId", -1);

        recyclerView = findViewById(R.id.recyclerView);
        searchInput = findViewById(R.id.searchInput);
        bottomNav = findViewById(R.id.bottom_navigation);

        db = new DBHelper(this);

        // SAME GRID AS HOME
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL
                )
        );

        // LOAD POSTS
        fullList = db.getPosts();

        // FALLBACK
        if (fullList == null || fullList.size() == 0) {

            fullList = new ArrayList<>();

            fullList.add(new Image(
                    "Demo 1",
                    R.drawable.download__12_
            ));

            fullList.add(new Image(
                    "Demo 2",
                    R.drawable.no_more_heroes_official_uaa_rankers_bible
            ));
        }

        filteredList = new ArrayList<>(fullList);

        // USE SAME ADAPTER AS HOME
        adapter = new ImageAdapter(this, filteredList, userId);

        recyclerView.setAdapter(adapter);

        // SEARCH FILTER
        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = s.toString().toLowerCase().trim();

                filteredList.clear();

                for (Image img : fullList) {

                    if (img.getTitle() != null &&
                            img.getTitle().toLowerCase().contains(query)) {

                        filteredList.add(img);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

        BottomNavHelper.setup(this, bottomNav, userId);

        bottomNav.setSelectedItemId(R.id.navsearch);
    }
}