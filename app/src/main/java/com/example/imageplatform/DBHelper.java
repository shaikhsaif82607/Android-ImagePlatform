package com.example.imageplatform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 👤 USERS
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE," +
                "password TEXT)");

        // 🖼 POSTS
        db.execSQL("CREATE TABLE posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "imageUri TEXT," +
                "userId INTEGER)");

        // ❤️ LIKES
        db.execSQL("CREATE TABLE likes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "postId INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS likes");
        onCreate(db);
    }

    // 🔐 REGISTER
    public boolean register(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
        if (c.moveToFirst()) {
            c.close();
            return false;
        }
        c.close();

        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);

        return db.insert("users", null, cv) != -1;
    }

    // 🔐 LOGIN
    public boolean login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean exists = c.moveToFirst();
        c.close();

        return exists;
    }

    // 🔥 GET USER ID
    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id FROM users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        int userId = -1;

        if (c.moveToFirst()) {
            userId = c.getInt(0);
        }

        c.close();
        return userId;
    }

    // 🔥 GET EMAIL FROM ID
    public String getUserEmail(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT email FROM users WHERE id=?",
                new String[]{String.valueOf(userId)}
        );

        String email = "User";

        if (c.moveToFirst()) {
            email = c.getString(0);
        }

        c.close();
        return email;
    }

    // 🖼 ADD POST
    public boolean addPost(String title, String uri, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("imageUri", uri);
        cv.put("userId", userId);

        return db.insert("posts", null, cv) != -1;
    }

    // 🖼 GET ALL POSTS
    public List<Image> getPosts() {
        List<Image> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM posts ORDER BY id DESC", null);

        while (c.moveToNext()) {
            list.add(new Image(
                    c.getInt(0),     // id
                    c.getString(1),  // title
                    c.getString(2)   // uri
            ));
        }

        c.close();
        return list;
    }

    // 🧑 USER POSTS
    public List<Image> getUserPosts(int userId) {
        List<Image> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM posts WHERE userId=? ORDER BY id DESC",
                new String[]{String.valueOf(userId)}
        );

        while (c.moveToNext()) {
            list.add(new Image(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2)
            ));
        }

        c.close();
        return list;
    }

    // ❌ DELETE POST (FINAL FIXED)
    public boolean deletePost(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("posts", "id=?", new String[]{String.valueOf(postId)});
        return rows > 0; // ✅ success check
    }

    // ❤️ LIKE
    public void likePost(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("postId", postId);

        db.insert("likes", null, cv);
    }

    // ❤️ LIKE COUNT
    public int getLikes(int postId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM likes WHERE postId=?",
                new String[]{String.valueOf(postId)}
        );

        int count = 0;

        if (c.moveToFirst()) {
            count = c.getInt(0);
        }

        c.close();
        return count;
    }
}