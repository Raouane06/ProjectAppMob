package com.example.labb1;

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
    private final List<Module> modules;

    public ModuleAdapter(List<Module> modules) {
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.bind(module);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    static class ModuleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvCoefficient, tvCredit;
        private final EditText etTd, etTp, etExam;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.moduleName);
            tvCoefficient = itemView.findViewById(R.id.moduleCoefficient);
            tvCredit = itemView.findViewById(R.id.moduleCredit);
            etTd = itemView.findViewById(R.id.tdScore);
            etTp = itemView.findViewById(R.id.tpScore);
            etExam = itemView.findViewById(R.id.examScore);
        }

        public void bind(Module module) {
            tvName.setText(module.getName());
            tvCoefficient.setText("Coef: " + module.getCoefficient());
            tvCredit.setText("Credit: " + module.getCredit());

            setupScoreField(etTd, module.hasTd(), module.getTd(), score -> module.setTd(score));
            setupScoreField(etTp, module.hasTp(), module.getTp(), score -> module.setTp(score));
            setupScoreField(etExam, true, module.getExam(), score -> module.setExam(score));
        }

        private void setupScoreField(EditText editText, boolean enabled, double currentScore,
                                     ScoreSetter scoreSetter) {
            editText.setEnabled(enabled);
            editText.setHint(enabled ? "Score" : "N/A");
            editText.setText(!Double.isNaN(currentScore) ? String.valueOf(currentScore) : "");

            editText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        try {
                            double score = Double.parseDouble(s.toString());
                            if (score >= 0 && score <= 20) {
                                scoreSetter.setScore(score);
                            } else {
                                Toast.makeText(itemView.getContext(),
                                        "Score must be 0-20", Toast.LENGTH_SHORT).show();
                                editText.setText("");
                            }
                        } catch (NumberFormatException e) {
                            editText.setText("");
                        }
                    }
                }
            });
        }

        interface ScoreSetter {
            void setScore(double score);
        }
    }
}
