package com.example.labb1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherProfileActivity extends AppCompatActivity {
    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    private database dbHelper;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_profile);

        // Initialize database helper
        dbHelper = new database(this);

        // Get current user data
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        currentUsername = prefs.getString(USERNAME_KEY, null);
        if (currentUsername == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load profile data
        loadProfileData();

        // Set up button listeners
        findViewById(R.id.edit_profile_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("username", currentUsername);
            startActivityForResult(intent, 100);
        });

        findViewById(R.id.logout_button).setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences(USER_PREFS, MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Set up bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, TeacherActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Already on profile
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_profile);
    }

    @SuppressLint("Range")
    private void loadProfileData() {
        Cursor cursor = dbHelper.getUserData(currentUsername);
        if (cursor != null && cursor.moveToFirst()) {
            // Set basic profile info
            TextView profileName = findViewById(R.id.profile_name);
            profileName.setText(currentUsername);

            TextView profileEmail = findViewById(R.id.Email);
            profileEmail.setText(cursor.getString(cursor.getColumnIndex(database.EMAIL)));

            // Set professional info (you might want to extend your database for these)
            TextView department = findViewById(R.id.profile_department);
            department.setText("Computer Science"); // Default or from DB

            TextView facultyId = findViewById(R.id.profile_faculty_id);
            facultyId.setText(getFacultyIdFromUsername(currentUsername));

            cursor.close();
        } else {
            Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFacultyIdFromUsername(String username) {
        // Implement your logic to convert username to faculty ID
        // For example: "prof_john" -> "CS-2025-001"
        return "CS-" + username.hashCode() % 10000; // Simple example
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Refresh data after profile edit
            currentUsername = data.getStringExtra("username");
            loadProfileData();
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}