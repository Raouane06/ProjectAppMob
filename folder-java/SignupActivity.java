package com.example.labb1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.labb1.databinding.SignupBinding;

public class SignupActivity extends AppCompatActivity {

    private SignupBinding binding;
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new database(this);

        binding.signupBtn.setOnClickListener(view -> {
            String email = binding.emailInput.getText().toString().trim();
            String username = binding.usernameInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (validateInputs(email, username, password)) {
                performSignup(email, username, password);
            }
        });

        binding.loginLink.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInputs(String email, String username, String password) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void performSignup(String email, String username, String password) {
        new Thread(() -> {
            boolean userExists = db.usernameExists(username);

            runOnUiThread(() -> {
                if (userExists) {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    String role = "student"; // Default role
                    boolean success = db.addUser (email, username, password, role);

                    if (success) {
                        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        navigateToLogin();
                    } else {
                        Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }).start();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
