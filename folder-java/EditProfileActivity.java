package com.example.labb1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editUsername, editEmail;
    private String currentUsername;
    private database dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        // Initialize database helper
        dbHelper = new database(this);

        // Initialize views
        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        Button saveButton = findViewById(R.id.save_profile_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        // Get current user data from intent
        Intent intent = getIntent();
        if (intent != null) {
            currentUsername = intent.getStringExtra("username");
            loadUserData(currentUsername);
        }

        // Set up listeners
        saveButton.setOnClickListener(v -> saveProfileChanges());
        cancelButton.setOnClickListener(v -> finish());
    }

    @SuppressLint("Range")
    private void loadUserData(String username) {
        Cursor cursor = dbHelper.getUserData(username);
        if (cursor != null && cursor.moveToFirst()) {
            editUsername.setText(cursor.getString(cursor.getColumnIndex(database.USERNAME)));
            editEmail.setText(cursor.getString(cursor.getColumnIndex(database.EMAIL)));
            cursor.close();
        }
    }

    @SuppressLint("Range")
    private void saveProfileChanges() {
        String newUsername = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        // Validate inputs
        if (newUsername.isEmpty()) {
            editUsername.setError("Username cannot be empty");
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Enter a valid email");
            return;
        }

        // Check if username is changed and already exists
        if (!newUsername.equals(currentUsername)) {
            if (dbHelper.usernameExists(newUsername)) {
                editUsername.setError("Username already taken");
                return;
            }
        }

        // Check if email is changed and already exists
        Cursor cursor = dbHelper.getUserData(currentUsername);
        String currentEmail = "";
        if (cursor != null && cursor.moveToFirst()) {
            currentEmail = cursor.getString(cursor.getColumnIndex(database.EMAIL));
            cursor.close();
        }

        if (!email.equals(currentEmail) && dbHelper.emailExists(email)) {
            editEmail.setError("Email already in use");
            return;
        }

        // Update profile
        boolean updated = dbHelper.updateUserProfile(currentUsername, newUsername, email);
        if (updated) {
            // Return updated data to calling activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("username", newUsername);
            resultIntent.putExtra("email", email);
            setResult(Activity.RESULT_OK, resultIntent);

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
