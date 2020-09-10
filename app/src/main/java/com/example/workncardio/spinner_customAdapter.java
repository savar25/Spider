package com.example.workncardio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.databases.FoodInfo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class spinner_customAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<FoodInfo> items;


    public spinner_customAdapter(@NonNull Context context, ArrayList<FoodInfo> items) {
        super(context, R.layout.fragment_calorie_food,0);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.items = items;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.fragment_calorie_food, parent, false);

        TextView Fname = (TextView) view.findViewById(R.id.FOName);
        TextView Fcal = (TextView) view.findViewById(R.id.Fcal);

        FoodInfo offerData = items.get(position);

       Fname.setText(offerData.getName());
       Fcal.setText(String.valueOf(offerData.getKcal()));

        return view;
    }

}
