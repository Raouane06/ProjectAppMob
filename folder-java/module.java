package com.example.lab1;

public class module {
    private String name;
    private int coefficient;
    private double td;
    private double tp;
    private double exam;
    public module(String name,int coefficient){
        this.name=name;
        this.coefficient=coefficient;
    }

    //setter
    public void getTd(float td){ this.td=td;}
    public void getTp(float tp){ this.tp=tp;}
    public void getExam(float exam){ this.exam=exam;}


    //getter
    public String getName(){ return name; }
    public int getCoefficient(){ return coefficient; }
    public double getTd(){ return td; }
    public double getTp(){ return tp; }
    public double getExam(){ return exam; }

}
