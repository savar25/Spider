package com.example.databases;

public class food_AnalysisInfo {

    String id;
    String Name;
    Double kcal;
    Integer Quant;

    public  food_AnalysisInfo(){}
    public food_AnalysisInfo(String id, String name, Double kcal, Integer quant) {
        this.id = id;
        Name = name;
        this.kcal = kcal;
        Quant = quant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public Integer getQuant() {
        return Quant;
    }

    public void setQuant(Integer quant) {
        Quant = quant;
    }
}
