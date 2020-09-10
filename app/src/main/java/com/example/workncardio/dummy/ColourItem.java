package com.example.workncardio.dummy;

import android.graphics.drawable.Drawable;

public class ColourItem {
    String name;
    Drawable drawable;

    public ColourItem(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
