package com.example.workncardio.dummy;

public class ProfileItem {

    String text;
    Integer icon;

    public ProfileItem(String text, Integer icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
