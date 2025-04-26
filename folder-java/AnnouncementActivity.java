package com.example.labb1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnnouncementActivity extends AppCompatActivity {
    private AnnouncementDBHelper dbHelper;
    private AnnouncementAdapterTeach adapter;
    private ArrayList<Announcement> announcements;
    private EditText etTitle, etContent;
    private SharedPreferences prefs;
    private String teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_announcement);

            // Initialize SharedPreferences
            prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            teacherName = "Prof. " + prefs.getString("username", "Teacher");

            // Initialize views
            ImageButton btnBack = findViewById(R.id.btnBack);
            etTitle = findViewById(R.id.etAnnouncement); // Removed local variable declaration
            etContent = findViewById(R.id.etAnnouncementContent); // Removed local variable
            Button btnPost = findViewById(R.id.btnPostAnnouncement);
            RecyclerView recyclerView = findViewById(R.id.recyclerAnnouncements);

            btnBack.setOnClickListener(v -> finish());

            // Initialize database helper (assign to class field)
            dbHelper = new AnnouncementDBHelper(this);
            announcements = new ArrayList<>();

            // Setup RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AnnouncementAdapterTeach(announcements); // Assign to class field
            recyclerView.setAdapter(adapter);

            // Load initial data
            loadAnnouncements();

            // Setup post button
            btnPost.setOnClickListener(v -> {
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                if (!title.isEmpty() && !content.isEmpty()) {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Announcement newAnnouncement = new Announcement(title, content, currentDate, teacherName);
                    postAnnouncement(newAnnouncement);
                } else {
                    Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("AnnouncementActivity", "Init error", e);
            Toast.makeText(this, "Initialization failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadAnnouncements() {
        try {
            announcements.clear();
            announcements.addAll(dbHelper.getAllAnnouncements());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("AnnouncementActivity", "Load error", e);
            Toast.makeText(this, "Failed to load announcements", Toast.LENGTH_SHORT).show();
        }
    }

    private void postAnnouncement(Announcement announcement) {
        try {
            dbHelper.addAnnouncement(announcement);
            announcements.add(0, announcement);
            adapter.notifyItemInserted(0);
            etTitle.setText("");
            etContent.setText("");
            Toast.makeText(this, "Announcement posted!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("AnnouncementActivity", "Post error", e);
            Toast.makeText(this, "Failed to post announcement", Toast.LENGTH_SHORT).show();
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
