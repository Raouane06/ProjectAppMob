package com.example.merry22;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Module {
    private String name;
    private int coefficient;
    private int credit;
    private double td;
    private double tp;
    private double exam;
    private boolean hasTd; // Indicates if the module has TD
    private boolean hasTp; // Indicates if the module has TP

    public Module() {
    }

    public Module(String name, int coefficient, int credit, boolean hasTd, boolean hasTp) {
        this.name = name;
        this.coefficient = coefficient;
        this.credit = credit;
        this.hasTd = hasTd;
        this.hasTp = hasTp;
    }

    // Load list of modules from the JSON file
    public List<Module> loadModules(Context context) {
        List<Module> modules = new ArrayList<>();
        try {
            String jsonString = readJsonFile(context);
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String moduleName = jsonObject.getString("Nom_module");
                int moduleCoefficient = jsonObject.getInt("Coefficient");
                int moduleCredit = jsonObject.getInt("Credit");
                boolean hasTd = jsonObject.getInt("td") == 1; // Check if TD is applicable
                boolean hasTp = jsonObject.getInt("tp") == 1; // Check if TP is applicable

                Module module = new Module(moduleName, moduleCoefficient, moduleCredit, hasTd, hasTp);
                modules.add(module);
            }
        } catch (Exception exp) {
            Log.e("Module", "Error loading modules: " + exp.getMessage());
        }
        return modules;
    }

    // Read JSON file from assets
    private String readJsonFile(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream input = assetManager.open("module.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception exp) {
            Log.e("Module", "Error reading JSON file: " + exp.getMessage());
            return "";
        }
    }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCoefficient(int coefficient) { this.coefficient = coefficient; }
    public void setCredit(int credit) { this.credit = credit; }
    public void setTd(double td) { this.td = td; }
    public void setTp(double tp) { this.tp = tp; }
    public void setExam(double exam) { this.exam = exam; }
    public void setHasTd(boolean hasTd) { this.hasTd = hasTd; }
    public void setHasTp(boolean hasTp) { this.hasTp = hasTp; }

    // Getters
    public String getName() { return name; }
    public int getCoefficient() { return coefficient; }
    public int getCredit() { return credit; }
    public double getTd() { return td; }
    public double getTp() { return tp; }
    public double getExam() { return exam; }
    public boolean hasTd() { return hasTd; } // Check if TD is applicable
    public boolean hasTp() { return hasTp; } // Check if TP is applicable
}
