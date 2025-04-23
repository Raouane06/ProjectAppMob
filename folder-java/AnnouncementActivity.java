package com.example.labb1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        dbHelper = new AnnouncementDBHelper(this);
        etTitle = findViewById(R.id.etAnnouncement);
        etContent = findViewById(R.id.etAnnouncementContent);
        Button btnPost = findViewById(R.id.btnPostAnnouncement);
        RecyclerView recyclerView = findViewById(R.id.recyclerAnnouncements);

        announcements = dbHelper.getAllAnnouncements();
        adapter = new AnnouncementAdapterTeach(announcements);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnPost.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (!title.isEmpty() && !content.isEmpty()) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String author = "Prof. Smith"; // Replace with actual author name

                Announcement newAnnouncement = new Announcement(title, content, currentDate, author);
                dbHelper.addAnnouncement(newAnnouncement);

                announcements.add(0, newAnnouncement);
                adapter.notifyItemInserted(0);
                etTitle.setText("");
                etContent.setText("");
                recyclerView.scrollToPosition(0);
            } else {
                Toast.makeText(this, "Please fill both title and content", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
