package com.example.imageplatform;

public class Image {

    int id; // 🔥 ADD THIS
    String title;
    int image;
    String imageUri;

    // 🔥 FROM DB (IMPORTANT)
    public Image(int id, String title, String imageUri) {
        this.id = id;
        this.title = title;
        this.imageUri = imageUri;
    }

    // OLD (fallback)
    public Image(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public Image(String title, String imageUri) {
        this.title = title;
        this.imageUri = imageUri;
    }



    public int getId() { return id; } // 🔥 ADD
    public String getTitle() { return title; }
    public int getImage() { return image; }
    public String getImageUri() { return imageUri; }
}