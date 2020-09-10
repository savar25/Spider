package com.example.workncardio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.databases.FoodInfo;
import com.example.databases.food_AnalysisInfo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class analysisRecyclerAdapter extends RecyclerView.Adapter<analysisRecyclerAdapter.ViewHolder> {

    private final ArrayList<food_AnalysisInfo> mValues;

    public analysisRecyclerAdapter(ArrayList<food_AnalysisInfo> mValues) {
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calc_element_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Fname.setText(mValues.get(position).getName());
        holder.Fcal.setText(String.valueOf(mValues.get(position).getKcal()));
        holder.FQuant.setText(String.valueOf(mValues.get(position).getQuant()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView Fname;
        public final TextView Fcal;
        public final TextView FQuant;

        public ViewHolder(View view) {
            super(view);
            Fname = (TextView) view.findViewById(R.id.food_name);
            Fcal = (TextView) view.findViewById(R.id.food_kcal);
            FQuant=view.findViewById(R.id.food_quant);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "Fname=" + Fname +
                    ", Fcal=" + Fcal +
                    '}';
        }
    }



}
