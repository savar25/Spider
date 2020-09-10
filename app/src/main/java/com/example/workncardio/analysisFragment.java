package com.example.workncardio;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;


public class analysisFragment extends Fragment {


    CombinedChart combinedChart;
    StepDatabase db;
    TextView current,left,advice;
    UserDets dets;
    UserInfo baseInfo;
    DataStepsModel stepsModel;
    ImageView shoe;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");
    DatabaseReference steps=firebaseDatabase.getReference("step").child(Homescreen.USER_NAME);
    private static final String TAG = "analysisFragment";
    public static String name="";
    Context context;
    ArrayList<DataStepsModel> temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_analysis, container, false);
        combinedChart=view.findViewById(R.id.combined_chart);
        db=new StepDatabase(getContext(),Homescreen.USER_NAME);
        current=view.findViewById(R.id.current);
        left=view.findViewById(R.id.stepLeft);
        advice=view.findViewById(R.id.advice);
        shoe=view.findViewById(R.id.shoe);

        context=getContext();
        Log.d(TAG, "onCreateView: "+Homescreen.USER_NAME);
        name=Homescreen.USER_NAME;
        return view;
    }






    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    UserInfo info=snapshot1.getValue(UserInfo.class);
                    Log.d(TAG, "onDataChange: "+Homescreen.USER_NAME);
                    if(info.getName().equals(name)){
                        baseInfo=info;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        steps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar calendar = Calendar.getInstance();
                String today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar.get(Calendar.MONTH)) + "_" + String.valueOf(calendar.get(Calendar.YEAR));
                stepsModel = snapshot.child(today).getValue(DataStepsModel.class);
               init();


               temp=new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "_" + String.valueOf(calendar.get(Calendar.MONTH)) + "_" + String.valueOf(calendar.get(Calendar.YEAR));
                    if(snapshot.child(date).getValue(DataStepsModel.class)!=null && !temp.contains(snapshot.child(date).getValue(DataStepsModel.class))) {
                        temp.add(snapshot.child(date).getValue(DataStepsModel.class));
                    }
                    calendar.setTimeInMillis(calendar.getTimeInMillis()-24*60*60*1000);
                }
                Log.d(TAG, "onDataChangeLOOP: "+temp);
                Collections.reverse(temp);
                setChart();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void init(){
        current.setText(String.valueOf(stepsModel.getSteps()));

        Integer base=baseInfo.getBaseStepGoal();
        Integer leftval=base-stepsModel.getSteps();

        Log.d(TAG, "init: "+context);
        if(leftval/base>=0.5){
            Drawable d=context.getResources().getDrawable(R.drawable.step_shortcut_icon,null);
            DrawableCompat.setTint(d,Color.parseColor("45bb32"));
            shoe.setImageDrawable(d);
            advice.setText("Have a cool walk and take rest :)");
            advice.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);

        }else {
            Drawable d=context.getResources().getDrawable(R.drawable.step_shortcut_icon,null);
            DrawableCompat.setTint(d,Color.parseColor("#00ff00"));
            shoe.setImageDrawable(d);
            advice.setText("Have a strongfold run, brisk walk, or a good gym session");
            advice.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);

        }
        Log.d(TAG, "onCreateView: "+ leftval);
        left.setText(String.valueOf(leftval));






    }

    public void setChart(){
        ArrayList<Entry> entries=new ArrayList<>();

        for(DataStepsModel vls:temp){
            Log.d(TAG, "init1: "+vls.dateNum);
            entries.add(new Entry(vls.dateNum,vls.steps));
        }

        ArrayList<BarEntry> barEntries=new ArrayList<>();
        for(DataStepsModel vls:temp){
            barEntries.add(new BarEntry(vls.dateNum,vls.steps));
        }

        LineDataSet lineDataSet=new LineDataSet(entries,"Linear Step Growth");
        lineDataSet.setColor(Color.rgb(131,245,44));
        lineDataSet.setLineWidth(5f);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setValueTextColor(Color.WHITE);

        LineData lineData=new LineData(lineDataSet);

        BarDataSet barDataSet=new BarDataSet(barEntries,"Discrete Step Growth");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.WHITE);
        BarData barData=new BarData(barDataSet);

        combinedChart.getDescription().setText("Test Description");
        combinedChart.setDrawGridBackground(false);
        combinedChart.setGridBackgroundColor(Color.WHITE);
        combinedChart.setDrawBarShadow(false);
        combinedChart.setHighlightFullBarEnabled(false);
        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});
        combinedChart.getXAxis().setDrawGridLines(false);
        combinedChart.getAxisLeft().setDrawGridLines(false);
        combinedChart.getAxisRight().setDrawGridLines(false);
        combinedChart.getXAxis().setTextColor(Color.WHITE);
        combinedChart.getAxisLeft().setTextColor(Color.WHITE);
        combinedChart.getAxisRight().setTextColor(Color.WHITE);
        combinedChart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        combinedChart.getLegend().setTextColor(Color.WHITE);
        combinedChart.getXAxis().setGranularityEnabled(true);
        combinedChart.getXAxis().setGranularity(1);
        combinedChart.getAxisRight().setGranularityEnabled(true);
        combinedChart.getAxisRight().setGranularity(1);
        combinedChart.getAxisLeft().setGranularityEnabled(true);
        combinedChart.getAxisLeft().setGranularity(1);

        CombinedData data=new CombinedData();
        data.setData(lineData);
        data.setData(barData);

        combinedChart.setData(data);
        combinedChart.invalidate();

    }
}