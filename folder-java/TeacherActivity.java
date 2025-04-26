package com.example.labb1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class TeacherActivity extends AppCompatActivity {

    private static final String TAG = "TeacherActivity";
    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";

    private BottomNavigationView bottomNav;
    private Fragment currentFragment;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main);

        // Initialize views
        welcomeText = findViewById(R.id.welcome_text);

        // Get teacher identification
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String username = prefs.getString(USERNAME_KEY, "");
        String teacherId = getIntent().getStringExtra("teacherId");
        String identifier = !username.isEmpty() ? username : teacherId;

        if (identifier != null && !identifier.isEmpty()) {
            welcomeText.setText("Welcome, Professor " + identifier + "!");
            loadModuleForTeacher(identifier);
        } else {
            Toast.makeText(this, "Teacher identification failed", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Set up bottom navigation
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadHomeFragment();
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Launch TeacherProfileActivity instead of loading profile fragment
                startActivity(new Intent(this, TeacherProfileActivity.class));
                return true;
            }
            return false;
        });

        // Set up button listeners
        findViewById(R.id.btnAnnouncements).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, AnnouncementActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Error opening announcements", Toast.LENGTH_SHORT).show();
                Log.e("TeacherActivity", "Activity error", e);
            }
        });

        findViewById(R.id.btnGradeManagement).setOnClickListener(v -> {
            Toast.makeText(this, "Grade Management feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnAttendance).setOnClickListener(v -> {
            Toast.makeText(this, "Attendance feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnSchedule).setOnClickListener(v -> {
            Toast.makeText(this, "Schedule feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnMaterials).setOnClickListener(v -> {
            Toast.makeText(this, "Teaching Materials feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Load the home fragment initially
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        // Show main content and hide fragment container
        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
        findViewById(R.id.fragment_container).setVisibility(View.GONE);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        setBottomNavVisibility(true);
    }

    public void setBottomNavVisibility(boolean visible) {
        if (bottomNav != null) {
            bottomNav.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (isFinishing() || isDestroyed()) return;

        // Hide main content and show fragment container
        findViewById(R.id.main_content).setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Check if the fragment is already added
        Fragment existingFragment = fm.findFragmentByTag(fragment.getClass().getSimpleName());
        if (existingFragment != null && existingFragment.isVisible()) {
            return;
        }

        // Set bottom nav visibility based on fragment type
        boolean showBottomNav = !(fragment instanceof Announcements) &&
                !(fragment instanceof calculate) &&
                !(fragment instanceof grades);
        setBottomNavVisibility(showBottomNav);

        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commitAllowingStateLoss();
        fm.executePendingTransactions();

        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            // Check if we're returning to home
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                loadHomeFragment();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void loadModuleForTeacher(String teacherId) {
        String moduleName = getModuleNameForTeacher(teacherId);
        if (moduleName != null) {
            TextView moduleText = findViewById(R.id.welcome_text);
            moduleText.setText("Welcome, Professor!\nModule: " + moduleName);
        }
    }

    private String getModuleNameForTeacher(String teacherId) {
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("module.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, "UTF-8");
            JSONArray arr = new JSONArray(jsonString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getString("teacherId").equals(teacherId)) {
                    return obj.getString("Nom_module");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading module.json", e);
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String username = prefs.getString(USERNAME_KEY, null);
        if (username != null) {
            welcomeText.setText("Welcome, Professor " + username + "!");
        }
    }
}
