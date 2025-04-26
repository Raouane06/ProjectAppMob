package com.example.labb1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.labb1.databinding.LoginBinding;

public class LoginActivity extends AppCompatActivity {

    LoginBinding binding;
    database db;
    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new database(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.usernameInput.getText().toString();
                String password = binding.passwordInput.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkCredentials = db.checkUserCredentials(username, password);

                    // In the onClick listener of binding.loginBtn:
                    if (checkCredentials) {
                        // Get user role from database
                        Cursor cursor = db.getUserData(username);
                        if (cursor != null && cursor.moveToFirst()) {
                            @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex(database.ROLE));
                            cursor.close();

                            SharedPreferences.Editor editor = getSharedPreferences(USER_PREFS, MODE_PRIVATE).edit();
                            editor.putString(USERNAME_KEY, username);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            // Redirect based on role
                            Intent intent;
                            if (role.equals("teacher")) {
                                // For teachers, go to TeacherActivity
                                intent = new Intent(LoginActivity.this, TeacherActivity.class);
                                // If you need to pass teacher ID (from username or other field)
                                intent.putExtra("teacherId", username); // or get from database
                            } else {
                                // For students, go to MainActivity as before
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
