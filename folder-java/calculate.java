package com.example.labb1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class calculate extends Fragment {
    private ModuleAdapter moduleAdapter;
    private List<Module> modules;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculate, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewModules);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);
        TextView tvResult = view.findViewById(R.id.tvResult);

        // Load modules from JSON
        Module moduleLoader = new Module();
        modules = moduleLoader.loadModules(requireContext());

        // Setup RecyclerView with your existing ModuleAdapter
        moduleAdapter = new ModuleAdapter(modules);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moduleAdapter);

        btnCalculate.setOnClickListener(v -> {
            double average = calculateWeightedAverage();
            int totalCredits = calculateTotalCredits();
            tvResult.setText(String.format("Average: %.2f\nCredits: %d", average, totalCredits));
        });

        // Bottom Navigation Setup
        BottomNavigationView bottomNav = view.findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_calculator); // Highlight calculator tab
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_calculator) {
                // Already on calculator fragment
                return true;
            }
            else if (itemId == R.id.nav_grades) {
                // Switch to grades fragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new grades())
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            else if (itemId == R.id.nav_profile) {
                // Switch to profile fragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new profile())
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            return false;
        });

        return view;
    }

    private double calculateWeightedAverage() {
        double totalWeightedScore = 0;
        double totalCoefficients = 0;

        for (Module module : modules) {
            double moduleScore = calculateModuleScore(module);
            if (!Double.isNaN(moduleScore)) {
                totalWeightedScore += moduleScore * module.getCoefficient();
                totalCoefficients += module.getCoefficient();
            }
        }

        return totalCoefficients > 0 ? totalWeightedScore / totalCoefficients : 0;
    }

    private double calculateModuleScore(Module module) {
        if (module.hasTd() && module.hasTp()) {
            // Module has both TD and TP
            if (Double.isNaN(module.getTd()) || Double.isNaN(module.getTp()) || Double.isNaN(module.getExam())) {
                return Double.NaN;
            }
            return (module.getTd() * 0.2) + (module.getTp() * 0.2) + (module.getExam() * 0.6);
        } else if (module.hasTd()) {
            // Module has only TD
            if (Double.isNaN(module.getTd()) || Double.isNaN(module.getExam())) {
                return Double.NaN;
            }
            return (module.getTd() * 0.4) + (module.getExam() * 0.6);
        } else if (module.hasTp()) {
            // Module has only TP
            if (Double.isNaN(module.getTp()) || Double.isNaN(module.getExam())) {
                return Double.NaN;
            }
            return (module.getTp() * 0.4) + (module.getExam() * 0.6);
        } else {
            // Module has only exam
            return !Double.isNaN(module.getExam()) ? module.getExam() : Double.NaN;
        }
    }

    private int calculateTotalCredits() {
        int totalCredits = 0;
        for (Module module : modules) {
            double moduleScore = calculateModuleScore(module);
            if (!Double.isNaN(moduleScore) && moduleScore >= 10) {
                totalCredits += module.getCredit();
            }
        }
        return totalCredits;
    }
}
