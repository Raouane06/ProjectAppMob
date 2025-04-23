package com.example.labb1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String USER_PREFS = "user_prefs";
    private static final String USERNAME_KEY = "username";
    private BottomNavigationView bottomNav;
    private Fragment currentFragment;

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

        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText("Hello, " + username);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadHomeFragment();
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(new profile(), true);
                return true;
            }
            return false;
        });
        // Add this to your onCreate() method
        findViewById(R.id.btnAnnouncements).setOnClickListener(v -> {
            loadFragment(new Announcements(), true);
        });

        findViewById(R.id.btnCalculate).setOnClickListener(v -> loadFragment(new calculate(), true));
        findViewById(R.id.btnGrades).setOnClickListener(v -> loadFragment(new grades(), true));
        findViewById(R.id.btnSchedule).setOnClickListener(v -> {
            loadFragment(new Schedule(), true);
        });

        // Load the home fragment initially
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            // Load a default home fragment if needed, or leave empty
            setBottomNavVisibility(true); // Ensure bottom nav is visible on "Home"
        }
    }

    public void setBottomNavVisibility(boolean visible) {
        if (bottomNav != null) {
            bottomNav.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (isFinishing() || isDestroyed()) return;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Check if the fragment is already added (optimization)
        Fragment existingFragment = fm.findFragmentByTag(fragment.getClass().getSimpleName());
        if (existingFragment != null && existingFragment.isVisible()) {
            return; // Already visible, no need to replace
        }
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
        setBottomNavVisibility(fragment instanceof profile || !(fragment instanceof calculate) && !(fragment instanceof grades) && !(fragment instanceof Announcements) && !(fragment instanceof Schedule));
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            // Update bottom nav visibility after back press
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            boolean showBottomNav = currentFragment == null ||
                    currentFragment instanceof profile;
            setBottomNavVisibility(showBottomNav);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String username = prefs.getString(USERNAME_KEY, null);
        if (username != null) {
            TextView welcomeText = findViewById(R.id.welcome_text);
            welcomeText.setText("Hello, " + username);
        }
    }
}
