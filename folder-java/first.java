package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class first extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(first.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity
        }, SPLASH_TIME_OUT);
    }
}
