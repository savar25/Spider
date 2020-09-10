package com.example.databases;

import android.widget.Adapter;

public class FoodInfo  {

    String id;
    String Name;
    Double kcal;
    public FoodInfo(){}

    public FoodInfo(String id, String name, Double kcal) {
        this.id = id;
        Name = name;
        this.kcal = kcal;
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
}
