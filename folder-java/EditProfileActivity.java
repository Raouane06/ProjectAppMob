package com.example.labb1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editUsername, editEmail, editRole;
    private String currentUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        editRole = findViewById(R.id.edit_role);

        // Get current user data from intent
        Intent intent = getIntent();
        if (intent != null) {
            currentUsername = intent.getStringExtra("username");
            editUsername.setText(currentUsername);
            editEmail.setText(intent.getStringExtra("email"));
            editRole.setText(intent.getStringExtra("role"));
        }

        // Save button
        findViewById(R.id.save_profile_button).setOnClickListener(v -> saveProfileChanges());

        // Cancel button
        findViewById(R.id.cancel_button).setOnClickListener(v -> finish());
    }

    @SuppressLint("Range")
    private void saveProfileChanges() {
        String newUsername = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String role = editRole.getText().toString().trim();

        if (newUsername.isEmpty() || email.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        database dbHelper = new database(this);

        // Check if username is changed and already exists
        if (!newUsername.equals(currentUsername) && dbHelper.usernameExists(newUsername)) {
            editUsername.setError("Username already taken");
            return;
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
        boolean updated = dbHelper.updateUserProfile(currentUsername, newUsername, email, role);
        if (updated) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newUsername", newUsername);
            setResult(Activity.RESULT_OK, resultIntent);
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}
