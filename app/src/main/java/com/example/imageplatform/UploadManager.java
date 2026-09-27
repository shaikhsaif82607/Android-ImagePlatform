package com.example.imageplatform;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UploadManager {

    private static final String PREF = "uploads_pref";
    private static final String KEY = "uploads";

    // ✅ SAVE (title + uri)
    public static void save(Context context, String title, String uri) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(KEY, new HashSet<>());

        Set<String> newSet = new HashSet<>(set);
        newSet.add(title + "||" + uri);

        prefs.edit().putStringSet(KEY, newSet).apply();
    }

    // ✅ GET RAW DATA
    public static Set<String> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY, new HashSet<>());
    }

    // 🔥🔥 NEW METHOD (THIS FIXES YOUR ERROR)
    public static List<Image> getImages(Context context) {
        List<Image> list = new ArrayList<>();

        Set<String> uploads = getAll(context);

        for (String data : uploads) {
            try {
                String[] parts = data.split("\\|\\|");

                String title = parts[0];
                String uri = parts[1];

                list.add(new Image(title, uri));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}