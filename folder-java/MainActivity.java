package com.example.lab1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView recyclerViewModules;
    private ModuleAdapter moduleAdapter;
    private List<Module> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewModules = findViewById(R.id.recyclerViewModules);
        recyclerViewModules.setLayoutManager(new LinearLayoutManager(this));

        Module moduleLoader = new Module();
        modules = moduleLoader.loadModules(this);

        moduleAdapter = new ModuleAdapter(modules);
        recyclerViewModules.setAdapter(moduleAdapter);
    }
}
