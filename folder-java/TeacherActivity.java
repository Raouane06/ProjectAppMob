package com.example.labb1;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class TeacherActivity extends AppCompatActivity {

    private static final String TAG = "TeacherActivity";

    private ImageButton btnGradeManagement,
            btnAttendance,
            btnSchedule,
            btnAnnouncements,
            btnStudentProgress,
            btnMaterials;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main);

        // 1) Reference your views
        welcomeText = findViewById(R.id.welcome_text);
        btnGradeManagement = findViewById(R.id.btnGradeManagement);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnAnnouncements = findViewById(R.id.btnAnnouncements);
        //btnStudentProgress = findViewById(R.id.btnStudentProgress);
        btnMaterials = findViewById(R.id.btnMaterials);

        // 2) Get the incoming teacherId
        String teacherId = getIntent().getStringExtra("teacherId");
        Log.d(TAG, "Received teacherId: " + teacherId);
        Toast.makeText(this, "Teacher ID: " + teacherId, Toast.LENGTH_SHORT).show();

        if (teacherId != null && !teacherId.isEmpty()) {
            loadModuleForTeacher(teacherId);
        } else {
            Toast.makeText(this, "No teacher ID provided", Toast.LENGTH_LONG).show();
        }

        // 3) Wire up your buttons
        btnGradeManagement.setOnClickListener(v ->
                Toast.makeText(this, "Grade Management feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        btnAttendance.setOnClickListener(v ->
                Toast.makeText(this, "Attendance feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        btnSchedule.setOnClickListener(v ->
                Toast.makeText(this, "Schedule feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        btnAnnouncements.setOnClickListener(v -> {
            Intent intent = new Intent(this, AnnouncementActivity.class);
            startActivity(intent);
        });


        btnStudentProgress.setOnClickListener(v ->
                Toast.makeText(this, "Student Progress feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        btnMaterials.setOnClickListener(v ->
                Toast.makeText(this, "Teaching Materials feature coming soon!", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadModuleForTeacher(String teacherId) {
        String moduleName = getModuleNameForTeacher(teacherId);

        if (moduleName != null) {
            // Update UI with module name
            welcomeText.setText("Welcome, Professor!\nModule: " + moduleName);
            Toast.makeText(this, "Module: " + moduleName, Toast.LENGTH_SHORT).show();
        } else {
            // If no module found
            Toast.makeText(this, "No module found for teacher ID: " + teacherId, Toast.LENGTH_LONG).show();
        }
    }

    private String getModuleNameForTeacher(String teacherId) {
        try {
            // Load JSON array from assets
            AssetManager am = getAssets();
            InputStream is = am.open("module.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, "UTF-8");
            JSONArray arr = new JSONArray(jsonString);

            // Loop through the modules and find the one matching the teacher's ID
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getString("teacherId").equals(teacherId)) {
                    return obj.getString("Nom_module");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading module.json", e);
        }
        return null; // Return null if no module found
    }
}
