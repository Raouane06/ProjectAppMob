package com.example.labb1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.labb1.LoginActivity;
import com.example.labb1.databinding.SignupBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

// ... imports

public class SignupActivity extends AppCompatActivity {

    private SignupBinding binding;
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new database(this);

        binding.teacherIdInput.setVisibility(View.GONE);

        binding.roleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.teacherRadio.getId()) {
                binding.teacherIdInput.setVisibility(View.VISIBLE);
            } else {
                binding.teacherIdInput.setVisibility(View.GONE);
            }
        });

        binding.signupBtn.setOnClickListener(view -> {
            String email = binding.emailInput.getText().toString().trim();
            String username = binding.usernameInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (validateInputs(email, username, password)) {
                int selectedId = binding.roleRadioGroup.getCheckedRadioButtonId();
                String role = "student";

                if (selectedId == binding.teacherRadio.getId()) {
                    String teacherId = binding.teacherIdInput.getText().toString().trim();
                    if (isTeacherIdValid(teacherId)) {
                        role = "teacher";
                    } else {
                        Toast.makeText(this, "Invalid Teacher ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Check uniqueness before inserting
                if (db.usernameExists(username)) {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (db.emailExists(email)) {
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                performSignup(email, username, password, role);
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
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isTeacherIdValid(String teacherId) {
        try {
            InputStream inputStream = getAssets().open("module.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject module = jsonArray.getJSONObject(i);
                String validTeacherId = module.getString("teacherId");
                if (validTeacherId.equals(teacherId)) {
                    return true;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void performSignup(String email, String username, String password, String role) {
        new Thread(() -> {
            boolean success = db.addUser(email, username, password, role);

            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(this, "Signup failed: DB error", Toast.LENGTH_SHORT).show();
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
