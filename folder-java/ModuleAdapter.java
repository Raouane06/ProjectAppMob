package com.example.lab1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>
{
    private List<Module> modules;

    public ModuleAdapter(List<Module> modules)
    {
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position)
    {
        Module module = modules.get(position);
        holder.moduleNameTextView.setText(module.getName());
        holder.moduleCoefficientTextView.setText("Coefficient: " + module.getCoefficient());
    }

    @Override
    public int getItemCount()
    {
        return modules.size();
    }


    public static class ModuleViewHolder extends RecyclerView.ViewHolder
    {
        TextView moduleNameTextView;
        TextView moduleCoefficientTextView;

        public ModuleViewHolder(@NonNull View itemView)
        {
            super(itemView);
            moduleNameTextView = itemView.findViewById(R.id.moduleName);
            moduleCoefficientTextView = itemView.findViewById(R.id.moduleCoefficient);
        }
    }
}
