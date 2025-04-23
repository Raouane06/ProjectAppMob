package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnnouncementActivity extends AppCompatActivity {

    private AnnouncementDBHelper dbHelper;
    private AnnouncementAdapter adapter;
    private ArrayList<String> announcements;
    private EditText etAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        dbHelper = new AnnouncementDBHelper(this);

        etAnnouncement = findViewById(R.id.etAnnouncement);
        Button btnPost = findViewById(R.id.btnPostAnnouncement);
        RecyclerView recyclerView = findViewById(R.id.recyclerAnnouncements);

        announcements = dbHelper.getAllAnnouncements();
        adapter = new AnnouncementAdapter(announcements);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnPost.setOnClickListener(v -> {
            String message = etAnnouncement.getText().toString().trim();
            if (!message.isEmpty()) {
                dbHelper.addAnnouncement(message);
                announcements.add(0, message); // Add to top
                adapter.notifyItemInserted(0);
                etAnnouncement.setText("");
                recyclerView.scrollToPosition(0);
            } else {
                Toast.makeText(this, "Please type an announcement.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
