package com.example.lab1;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Module
{
    private String name;
    private int coefficient;
    private double td;
    private double tp;
    private double exam;
    public Module() {
    }
    public Module(String name,int coefficient) {
        this.name=name;
        this.coefficient=coefficient;
    }

    public List<Module> loadModules(Context context)
    {
        List<Module> modules = new ArrayList<>();
        try {
            String jsonString = readJsonFile(context);
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Module module = new Module
                (
                        jsonObject.getString("name"),
                        jsonObject.getInt("coefficient")
                );
                modules.add(module);
            }
        } catch (Exception exp)
        {
            Log.e("Module", "Error loading modules: " + exp.getMessage());
        }
        return modules;
    }


    public void setName(String name) { this.name = name; }
    public void setCoefficient(int coefficient) { this.coefficient = coefficient; }
    public void setTd(double td) { this.td = td; }
    public void setTp(double tp) { this.tp = tp; }
    public void setExam(double exam) { this.exam = exam; }

    public String getName() { return name; }
    public int getCoefficient() { return coefficient; }
    public double getTd() { return td; }
    public double getTp() { return tp; }
    public double getExam() { return exam; }

    private String readJsonFile(Context context)
    {
        try {
            InputStream input = context.getResources().openRawResource(R.raw.module);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception exp) {
            Log.e("Module", "Error reading JSON file: " + exp.getMessage());
            return "";
        }
    }
}
