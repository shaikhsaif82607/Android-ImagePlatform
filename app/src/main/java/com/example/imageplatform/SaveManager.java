package com.example.imageplatform;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SaveManager {

    private static final String PREF_NAME = "saved_images";
    private static final String KEY_SAVED = "images";

    // 🔥 SAVE IMAGE
    public static void save(Context context, String data) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_SAVED, new HashSet<>()));

        set.add(data);

        prefs.edit().putStringSet(KEY_SAVED, set).apply();
    }

    // 🔥 REMOVE IMAGE
    public static void remove(Context context, String data) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_SAVED, new HashSet<>()));

        set.remove(data);

        prefs.edit().putStringSet(KEY_SAVED, set).apply();
    }

    // 🔥 CHECK IF SAVED
    public static boolean isSaved(Context context, String data) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(KEY_SAVED, new HashSet<>());

        return set.contains(data);
    }

    // 🔥 GET ALL SAVED
    public static Set<String> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_SAVED, new HashSet<>());
    }
}