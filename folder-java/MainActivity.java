package com.example.merry22;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewModules;
    private ModuleAdapter moduleAdapter;
    private List<Module> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewModules = findViewById(R.id.recyclerViewModules);
        recyclerViewModules.setLayoutManager(new LinearLayoutManager(this));

        Module moduleLoader = new Module();
        modules = moduleLoader.loadModules(this);

        moduleAdapter = new ModuleAdapter(modules);
        recyclerViewModules.setAdapter(moduleAdapter);

        Button btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(view -> {
            double sumWeightedAverages = 0;
            int sumCoefficients = 0;

            for (Module module : modules) {
                double moduleAverage = ((module.getTd() + module.getTp()) / 2 + module.getExam()) / 2;
                sumWeightedAverages += moduleAverage * module.getCoefficient();
                sumCoefficients += module.getCoefficient();
            }

            double weightedAverage = sumWeightedAverages / sumCoefficients;
            TextView tvResult = findViewById(R.id.tvResult);
            if (weightedAverage >= 10) {
                tvResult.setTextColor(ContextCompat.getColor(this, R.color.green));
            } else {
                tvResult.setTextColor(ContextCompat.getColor(this, R.color.red));
            }
            tvResult.setText("Result: " + weightedAverage + (weightedAverage >= 10 ? " (Pass)" : " (Fail)"));
        });
    }
}
