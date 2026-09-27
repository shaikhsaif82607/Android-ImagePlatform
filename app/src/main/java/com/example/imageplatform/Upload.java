package com.example.imageplatform;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class Upload extends AppCompatActivity {

    ImageView previewImage;
    EditText titleInput;
    MaterialButton selectBtn, uploadBtn;
    BottomNavigationView bottomNav;

    Uri imageUri;
    DBHelper db;

    int userId; // 🔥 USER ID

    // 🔥 IMAGE PICKER
    private final ActivityResultLauncher<Intent> imagePicker =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null &&
                        result.getData().getData() != null) {

                    imageUri = result.getData().getData();

                    // ✅ keep permission
                    getContentResolver().takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    previewImage.setImageURI(imageUri);
                } else {
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // 🔥 GET USER ID FROM LOGIN
        userId = getIntent().getIntExtra("userId", -1);

        // 🔍 INIT
        previewImage = findViewById(R.id.previewImage);
        titleInput = findViewById(R.id.titleInput);
        selectBtn = findViewById(R.id.selectBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        bottomNav = findViewById(R.id.bottom_navigation);

        db = new DBHelper(this);

        // 🔻 NAVIGATION (WITH USER)
        BottomNavHelper.setup(this, bottomNav, userId);
        bottomNav.setSelectedItemId(R.id.navadd);

        // 🔥 SELECT IMAGE
        selectBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

            imagePicker.launch(intent);
        });

        // 🔥 UPLOAD POST
        uploadBtn.setOnClickListener(v -> {

            String title = titleInput.getText().toString().trim();

            if (imageUri == null) {
                Toast.makeText(this, "Select image first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (title.isEmpty()) {
                Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userId == -1) {
                Toast.makeText(this, "User error. Login again.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ SAVE WITH USER ID
            boolean result = db.addPost(title, imageUri.toString(), userId);

            if (result) {
                Toast.makeText(this, "Uploaded!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}