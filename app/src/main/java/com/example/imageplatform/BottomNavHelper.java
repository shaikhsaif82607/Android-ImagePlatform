package com.example.imageplatform;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    // ✅ OLD METHOD (kept so app doesn't crash)
    public static void setup(Activity activity, BottomNavigationView bottomNav) {
        setup(activity, bottomNav, -1); // default userId
    }

    // ✅ NEW METHOD (MAIN)
    public static void setup(Activity activity, BottomNavigationView bottomNav, int userId) {

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.navhome) {
                if (!(activity instanceof Home)) {
                    intent = new Intent(activity, Home.class);
                }

            } else if (id == R.id.navsearch) {
                if (!(activity instanceof Search)) {
                    intent = new Intent(activity, Search.class);
                }

            } else if (id == R.id.navadd) {
                if (!(activity instanceof Upload)) {
                    intent = new Intent(activity, Upload.class);
                }

            } else if (id == R.id.navsaved) {
                if (!(activity instanceof Saved)) {
                    intent = new Intent(activity, Saved.class);
                }

            } else if (id == R.id.navprofile) {
                if (!(activity instanceof Profile)) {
                    intent = new Intent(activity, Profile.class);
                }
            }

            // 🔥 PASS USER ID EVERYWHERE
            if (intent != null) {
                intent.putExtra("userId", userId);
                activity.startActivity(intent);
            }

            return true;
        });
    }
}