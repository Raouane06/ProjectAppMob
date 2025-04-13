package com.example.labb1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String username = prefs.getString(USERNAME_KEY, null);

        if (username == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        //welcome message
        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText("Hello, " + username);

        //load calsulate page
        ImageButton btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(v -> {
            loadFragment(new calculate());
        });
        //load grades page
        ImageButton btnGrades = findViewById(R.id.btnGrades);
        btnGrades.setOnClickListener(v -> {
            loadFragment(new grades());
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update username if changed
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String username = prefs.getString(USERNAME_KEY, null);
        if (username != null) {
            TextView welcomeText = findViewById(R.id.welcome_text);
            welcomeText.setText("Hello, " + username);
        }
    }
}
