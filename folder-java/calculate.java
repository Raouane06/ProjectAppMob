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

        // Load modules
        Module moduleLoader = new Module();
        modules = moduleLoader.loadModules(requireContext());

        moduleAdapter = new ModuleAdapter(modules);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moduleAdapter);

        view.findViewById(R.id.btnBackToMain).setOnClickListener(v -> {
            if (getActivity() != null) {
                // This ensures proper bottom nav visibility restoration
                getActivity().onBackPressed();
            }
        });

        btnCalculate.setOnClickListener(v -> {
            double average = calculateWeightedAverage();
            int totalCredits = calculateTotalCredits();
            tvResult.setText(String.format("Average: %.2f\nCredits: %d", average, totalCredits));
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
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavVisibility(true);
        }
    }
}
