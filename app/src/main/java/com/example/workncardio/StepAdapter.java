package com.example.workncardio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private static final String TAG = "StepAdapter";

    ArrayList<DataStepsModel> stepCountList=new ArrayList();
    Context context;

    public StepAdapter(ArrayList<DataStepsModel> stepCountList, Context context) {
        this.stepCountList = stepCountList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.element_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " +stepCountList.get(position));
        holder.Date.setText(String.valueOf(stepCountList.get(position).date));
        holder.Steps.setText(String.valueOf(stepCountList.get(position).steps));
    }

    @Override
    public int getItemCount() {
        return stepCountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Date,Steps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Date=(TextView)itemView.findViewById(R.id.Date);
            Steps=itemView.findViewById(R.id.Steps);
        }
    }

    public void note(){
        notifyDataSetChanged();
    }
}
