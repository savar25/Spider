package com.example.workncardio;

public class DataStepsModel {

    public Integer dateNum;
    public String date;
    public Integer steps;

    public DataStepsModel(){}
    public DataStepsModel(Integer dateNum, String date, Integer steps) {
        this.dateNum = dateNum;
        this.date = date;
        this.steps = steps;
    }

    public Integer getDateNum() {
        return dateNum;
    }

    public void setDateNum(Integer dateNum) {
        this.dateNum = dateNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }
}

