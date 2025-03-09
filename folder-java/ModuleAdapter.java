package com.example.merry22;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
    private List<Module> modules;

    public ModuleAdapter(List<Module> modules) {
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);

        // Bind module name, coefficient, and credit
        holder.moduleNameTextView.setText(module.getName());
        holder.moduleCoefficientTextView.setText("Coef: " + module.getCoefficient());
        holder.moduleCreditTextView.setText(" | Credit: " + module.getCredit());

        // Enable/disable TD EditText based on module's hasTd property
        if (module.hasTd()) {
            holder.tdScore.setEnabled(true);
            holder.tdScore.setHint("TD score");
            holder.tdScore.setText(!Double.isNaN(module.getTd()) ? String.valueOf(module.getTd()) : "");
        } else {
            holder.tdScore.setEnabled(false);
            holder.tdScore.setHint("N/A");
            holder.tdScore.setText("");
        }

        // Enable/disable TP EditText based on module's hasTp property
        if (module.hasTp()) {
            holder.tpScore.setEnabled(true);
            holder.tpScore.setHint("TP score");
            holder.tpScore.setText(!Double.isNaN(module.getTp()) ? String.valueOf(module.getTp()) : "");
        } else {
            holder.tpScore.setEnabled(false);
            holder.tpScore.setHint("N/A");
            holder.tpScore.setText("");
        }

        // Always enable Exam EditText
        holder.examScore.setEnabled(true);
        holder.examScore.setHint("Exam score");
        holder.examScore.setText(!Double.isNaN(module.getExam()) ? String.valueOf(module.getExam()) : "");

        // Add TextWatchers for TD, TP, and Exam scores
        holder.tdScore.addTextChangedListener(new ScoreTextWatcher(holder, module, ScoreType.TD));
        holder.tpScore.addTextChangedListener(new ScoreTextWatcher(holder, module, ScoreType.TP));
        holder.examScore.addTextChangedListener(new ScoreTextWatcher(holder, module, ScoreType.EXAM));
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView moduleNameTextView;
        TextView moduleCoefficientTextView;
        TextView moduleCreditTextView;
        EditText tdScore, tpScore, examScore;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleNameTextView = itemView.findViewById(R.id.moduleName);
            moduleCoefficientTextView = itemView.findViewById(R.id.moduleCoefficient);
            moduleCreditTextView = itemView.findViewById(R.id.moduleCredit);
            tdScore = itemView.findViewById(R.id.tdScore);
            tpScore = itemView.findViewById(R.id.tpScore);
            examScore = itemView.findViewById(R.id.examScore);
        }
    }

    private static class ScoreTextWatcher implements TextWatcher {
        private final ModuleViewHolder holder;
        private final Module module;
        private final ScoreType type;

        public ScoreTextWatcher(ModuleViewHolder holder, Module module, ScoreType type) {
            this.holder = holder;
            this.module = module;
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String input = charSequence.toString().trim();
            if (!input.isEmpty()) {
                try {
                    double score = Double.parseDouble(input);
                    if (score >= 0 && score <= 20) {
                        switch (type) {
                            case TD:
                                module.setTd(score);
                                break;
                            case TP:
                                module.setTp(score);
                                break;
                            case EXAM:
                                module.setExam(score);
                                break;
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Score must be between 0 and 20", Toast.LENGTH_SHORT).show();
                        resetInputField();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(holder.itemView.getContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
                    resetInputField();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}

        private void resetInputField() {
            switch (type) {
                case TD:
                    holder.tdScore.setText("");
                    break;
                case TP:
                    holder.tpScore.setText("");
                    break;
                case EXAM:
                    holder.examScore.setText("");
                    break;
            }
        }
    }

    private enum ScoreType {
        TD, TP, EXAM
    }
}
