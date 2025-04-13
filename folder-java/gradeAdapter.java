package com.example.labb1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class gradeAdapter extends RecyclerView.Adapter<gradeAdapter.GradeViewHolder> {
    private final List<Module> modules;

    public gradeAdapter(List<Module> modules) {
        this.modules = modules;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.moduleName.setText(module.getName());
        holder.tdGrade.setText("TD: " + module.getTd());
        holder.tpGrade.setText("TP: " + module.getTp());
        holder.examGrade.setText("Exam: " + module.getExam());

        // Calculate and display average
        double avg = ((module.getTd() + module.getTp())/2 + module.getExam())/2;
        holder.moduleAverage.setText(String.format("Average: %.2f", avg));
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView moduleName, tdGrade, tpGrade, examGrade, moduleAverage;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.module_name);
            tdGrade = itemView.findViewById(R.id.td_grade);
            tpGrade = itemView.findViewById(R.id.tp_grade);
            examGrade = itemView.findViewById(R.id.exam_grade);
            moduleAverage = itemView.findViewById(R.id.module_average);
        }
    }
}
