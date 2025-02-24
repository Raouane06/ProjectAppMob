package com.example.lab1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        recyclerViewModules = findViewById(R.id.recyclerViewModules);
        recyclerViewModules.setLayoutManager(new LinearLayoutManager(this));

        // Dummy empty adapter to see UI without functionality
        recyclerViewModules.setAdapter(new DummyAdapter());
    }
}
