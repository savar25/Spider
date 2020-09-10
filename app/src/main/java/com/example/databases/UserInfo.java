package com.example.databases;

public class UserInfo {

    String ID;
    String name;
    String eID;
    Integer age,workHour,NOM,BaseStepGoal;
    Float Weight,Height;
    Double BMI;
    Integer background;
    boolean pic;

    public UserInfo(){}

    public UserInfo(String ID, String name, String eID, Integer age, Integer workHour, Integer NOM, Integer baseStepGoal, Float weight, Float height, Double BMI, Integer background, boolean pic) {
        this.ID = ID;
        this.name = name;
        this.eID = eID;
        this.age = age;
        this.workHour = workHour;
        this.NOM = NOM;
        BaseStepGoal = baseStepGoal;
        Weight = weight;
        Height = height;
        this.BMI = BMI;
        this.background = background;
        this.pic = pic;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getBackground() {
        return background;
    }

    public void setBackground(Integer background) {
        this.background = background;
    }

    public boolean isPic() {
        return pic;
    }

    public void setPic(boolean pic) {
        this.pic = pic;
    }

    public Integer getBaseStepGoal() {
        return BaseStepGoal;
    }

    public void setBaseStepGoal(Integer baseStepGoal) {
        BaseStepGoal = baseStepGoal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteID() {
        return eID;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWorkHour() {
        return workHour;
    }

    public void setWorkHour(Integer workHour) {
        this.workHour = workHour;
    }

    public Integer getNOM() {
        return NOM;
    }

    public void setNOM(Integer NOM) {
        this.NOM = NOM;
    }

    public Float getWeight() {
        return Weight;
    }

    public void setWeight(Float weight) {
        Weight = weight;
    }

    public Float getHeight() {
        return Height;
    }

    public void setHeight(Float height) {
        Height = height;
    }

    public Double getBMI() {
        return BMI;
    }

    public void setBMI(Double BMI) {
        this.BMI = BMI;
    }
}
