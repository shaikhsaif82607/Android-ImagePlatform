package com.example.imageplatform;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailEdit, passwordEdit;
    MaterialButton loginBtn;
    TextView goRegister;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔍 INIT
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginBtn = findViewById(R.id.loginBtn);
        goRegister = findViewById(R.id.goRegister);

        db = new DBHelper(this);

        // 🔐 LOGIN BUTTON
        loginBtn.setOnClickListener(v -> {

            String email = emailEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            // ❌ EMPTY CHECK
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ❌ EMAIL FORMAT CHECK (🔥 improvement)
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔐 LOGIN CHECK
            if (db.login(email, password)) {

                // ✅ GET USER ID (IMPORTANT)
                int userId = db.getUserId(email, password);

                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                // ✅ PASS USER ID TO HOME
                Intent intent = new Intent(LoginActivity.this, Home.class);
                intent.putExtra("userId", userId);

                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // 🔁 GO TO REGISTER
        goRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}