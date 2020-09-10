package com.example.workncardio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.databases.FoodDatabase;
import com.example.databases.FoodInfo;
import com.example.databases.food_AnalysisInfo;
import com.example.databases.food_analysisDatabase;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class calCalc extends Fragment {

    static Spinner Breakfast_spinner,Lunch_spinner,Dinner_spinner;
    static RecyclerView recB,recL,recD;
    static FoodDatabase database;
    static food_analysisDatabase food_analysisDatabaseB,food_analysisDatabaseL,food_analysisDatabaseD;
    static analysisRecyclerAdapter aB,aL,aD;
    static Context context;
    static TextView netB,netL,netD;
    static TextView adviceB,adviceL,adviceD;
    static Double netvB,netvL,netvD;
    private static final String TAG = "calCalc";
    static FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    static DatabaseReference foodData=firebaseDatabase.getReference("Food Data").child(Homescreen.USER_NAME);
    static DatabaseReference foodAnalysisB=firebaseDatabase.getReference("Food Analysis BreakFast").child(Homescreen.USER_NAME);
    static DatabaseReference foodAnalysisL=firebaseDatabase.getReference("Food Analysis Lunch").child(Homescreen.USER_NAME);
    static DatabaseReference foodAnalysisD=firebaseDatabase.getReference("Food Analysis Dinner").child(Homescreen.USER_NAME);
    static ArrayList<food_AnalysisInfo> Bpresent,Lpresent,Dpresent;
    static  ArrayList<FoodInfo> foodInfos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cal_calc, container, false);


        Breakfast_spinner=view.findViewById(R.id.Breakfastspinner);
        Lunch_spinner=view.findViewById(R.id.Lunchspinner);
        Dinner_spinner=view.findViewById(R.id.dinnerspinner);

        context=getContext();
        foodInfos=new ArrayList<>();
        Bpresent=new ArrayList<>();
        Lpresent=new ArrayList<>();
        Dpresent=new ArrayList<>();


        recB=view.findViewById(R.id.break_fastrec);
        recB.setLayoutManager(new LinearLayoutManager(getContext()));

        recL=view.findViewById(R.id.lunchrec);
        recL.setLayoutManager(new LinearLayoutManager(getContext()));

        recD=view.findViewById(R.id.dinnerrec);
        recD.setLayoutManager(new LinearLayoutManager(getContext()));

        netB=view.findViewById(R.id.netCalcB);
        netL=view.findViewById(R.id.netCalcL);
        netD=view.findViewById(R.id.netCalcD);

        adviceB=view.findViewById(R.id.breakfast_advice);
        adviceL=view.findViewById(R.id.lunch_advice);
        adviceD=view.findViewById(R.id.dinner_advice);















        Breakfast_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            FoodInfo foodInfo;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodInfo = new FoodInfo();
                if (!adapterView.getSelectedItem().toString().equals("Choose")) {
                    for (FoodInfo foodInfo1 : foodInfos) {
                        if (adapterView.getSelectedItem().equals(foodInfo1.getName())) {
                            foodInfo = foodInfo1;
                        }
                    }

                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View promptsView = li.inflate(R.layout.quantity_alert_dialogue, null);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    alertDialogBuilder.setView(promptsView);

                    final EditText foodInput = (EditText) promptsView
                            .findViewById(R.id.quantityVal);
                    final TextView foodItem=promptsView.findViewById(R.id.item);
                    foodItem.setText(foodInfo.getName());
                    final View view1 = getView();
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("ADD",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            Integer tempQuant = Integer.parseInt(foodInput.getText().toString());
                                            foodAnalysisB.child(foodInfo.getId()).setValue(new food_AnalysisInfo(foodInfo.getId(),foodInfo.getName(),foodInfo.getKcal(),tempQuant));
                                            Snackbar.make(view1, "Food Item Added", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            note();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }

                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }else {
                    //No work
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Lunch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            FoodInfo foodInfo;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodInfo = new FoodInfo();
                if (!adapterView.getSelectedItem().toString().equals("Choose")) {
                    for (FoodInfo foodInfo1 : foodInfos) {
                        if (adapterView.getSelectedItem().equals(foodInfo1.getName())) {
                            foodInfo = foodInfo1;
                        }
                    }

                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View promptsView = li.inflate(R.layout.quantity_alert_dialogue, null);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    alertDialogBuilder.setView(promptsView);

                    final EditText foodInput = (EditText) promptsView
                            .findViewById(R.id.quantityVal);
                    final TextView foodItem=promptsView.findViewById(R.id.item);
                    foodItem.setText(foodInfo.getName());
                    final View view1 = getView();
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("ADD",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            Integer tempQuant = Integer.parseInt(foodInput.getText().toString());
                                            foodAnalysisL.child(foodInfo.getId()).setValue(new food_AnalysisInfo(foodInfo.getId(),foodInfo.getName(),foodInfo.getKcal(),tempQuant));
                                            Snackbar.make(view1, "Food Item Added", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            note();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }

                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }else {
                    //No work
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Dinner_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            FoodInfo foodInfo;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodInfo = new FoodInfo();
                if (!adapterView.getSelectedItem().toString().equals("Choose")) {
                    for (FoodInfo foodInfo1 : foodInfos) {
                        if (adapterView.getSelectedItem().equals(foodInfo1.getName())) {
                            foodInfo = foodInfo1;
                        }
                    }
                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View promptsView = li.inflate(R.layout.quantity_alert_dialogue, null);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    alertDialogBuilder.setView(promptsView);

                    final EditText foodInput = (EditText) promptsView
                            .findViewById(R.id.quantityVal);
                    final TextView foodItem=promptsView.findViewById(R.id.item);
                    foodItem.setText(foodInfo.getName());
                    final View view1 = getView();
                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("ADD",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text
                                            Integer tempQuant = Integer.parseInt(foodInput.getText().toString());
                                            foodAnalysisD.child(foodInfo.getId()).setValue(new food_AnalysisInfo(foodInfo.getId(),foodInfo.getName(),foodInfo.getKcal(),tempQuant));
                                            Snackbar.make(view1, "Food Item Added", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                            note();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }

                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }else {
                    //No work
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        new ItemTouchHelper(callbackB).attachToRecyclerView(recB);
        new ItemTouchHelper(callbackL).attachToRecyclerView(recL);
        new ItemTouchHelper(callbackD).attachToRecyclerView(recD);




        return view;
    }


    public static void note() {

        netB.setText(String.format("%.3f",netCalB()));
        netL.setText(String.format("%.3f",netCalL()));
        netD.setText(String.format("%.3f",netCalD()));

        String stringB="";
        if(netCalB()<0.3){
            stringB=stringB.concat("Very Low Breakfast Calorie Intake.Increase Calories");
        }else if(netCalB()>=0.3 && netCalB()<0.5){
            stringB=stringB.concat("Optimum Calorie Inflow");
        }else{
            stringB=stringB.concat("Very High Breakfast Calorie Intake.Decrease Calories");
        }

        if(Bpresent.size()<=2){
            stringB=stringB.concat("\nNo Diversity.Add more items");
        }else if(Bpresent.size()>2 && Bpresent.size()<=6){
            stringB=stringB.concat("O\nptimum Diversity");
        }else {
            stringB=stringB.concat("\nHigh Diversity.Reduce Excess Inflow");
        }
        Log.d(TAG, "note: "+stringB);
        adviceB.setText(stringB);

        String stringL="";
        if(netCalL()<0.4){
            stringL=stringL.concat("Very Low Lunch Calorie Intake.Increase Calories");
        }else if(netCalL()>=0.4 && netCalL()<0.7){
            stringL=stringL.concat("Optimum Calorie Inflow");
        }else{
            stringL=stringL.concat("Very High Lunch Calorie Intake.Decrease Calories");
        }

        if(Lpresent.size()<=2){
            stringL=stringL.concat("\nNo Diversity.Add more items");
        }else if(Lpresent.size()>2 && Lpresent.size()<=6){
            stringL=stringL.concat("\nOptimum Diversity");
        }else {
            stringL=stringL.concat("\nHigh Diversity.Reduce Excess Inflow");
        }

        adviceL.setText(stringL);

        String stringD="";
        if(netCalD()<0.3){
            stringD=stringD.concat("Very Low Dinner Calorie Intake.Increase Calories");
        }else if(netCalD()>=0.3 && netCalD()<0.6){
            stringD=stringD.concat("Optimum Calorie Inflow");
        }else{
            stringD=stringD.concat("Very High Dinner Calorie Intake.Decrease Calories");
        }

        if(Dpresent.size()<=2){
            stringD=stringD.concat("\nNo Diversity.Add more items");
        }else if(Dpresent.size()>2 && Dpresent.size()<=6){
            stringD=stringD.concat("\nOptimum Diversity");
        }else {
            stringD=stringD.concat("\nHigh Diversity.Reduce Excess Inflow");
        }

        adviceD.setText(stringD);


    }

    ItemTouchHelper.SimpleCallback callbackB=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            ArrayList<food_AnalysisInfo> foodInfos=Bpresent;
            food_AnalysisInfo foodInfo2=foodInfos.get(viewHolder.getAdapterPosition());
            foodAnalysisB.child(foodInfo2.getId()).removeValue();

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(Color.RED)
                    .addSwipeLeftLabel("Remove")
                    .addSwipeLeftActionIcon(android.R.drawable.ic_menu_delete)
                    .setSwipeLeftLabelTextSize((int) TypedValue.COMPLEX_UNIT_DIP,15)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    ItemTouchHelper.SimpleCallback callbackL=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            ArrayList<food_AnalysisInfo> foodInfos=Lpresent;
            food_AnalysisInfo foodInfo2=foodInfos.get(viewHolder.getAdapterPosition());
            foodAnalysisL.child(foodInfo2.getId()).removeValue();

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(Color.RED)
                    .addSwipeLeftLabel("Remove")
                    .addSwipeLeftActionIcon(android.R.drawable.ic_menu_delete)
                    .setSwipeLeftLabelTextSize((int) TypedValue.COMPLEX_UNIT_DIP,15)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    ItemTouchHelper.SimpleCallback callbackD=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            ArrayList<food_AnalysisInfo> foodInfos=Dpresent;
            food_AnalysisInfo foodInfo2=foodInfos.get(viewHolder.getAdapterPosition());
            foodAnalysisD.child(foodInfo2.getId()).removeValue();

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(Color.RED)
                    .addSwipeLeftLabel("Remove")
                    .addSwipeLeftActionIcon(android.R.drawable.ic_menu_delete)
                    .setSwipeLeftLabelTextSize((int) TypedValue.COMPLEX_UNIT_DIP,10)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    /*public static void notice(){
        final ArrayList<FoodInfo> foodInfos=database.getVals();
        ArrayList<String> foodNames=new ArrayList<>();
        foodNames.add(0,"Choose");
        for(FoodInfo foodInfo:foodInfos){
            foodNames.add(foodInfo.getName());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,foodNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Breakfast_spinner.setAdapter(adapter);
        Lunch_spinner.setAdapter(adapter);
        Dinner_spinner.setAdapter(adapter);
    }*/

    public static Double netCalB(){
         netvB=0.0;

         for(food_AnalysisInfo f:Bpresent){
             netvB+=f.getKcal()*f.getQuant();
         }
        return netvB;
    }

    public static Double netCalL(){
        netvL=0.0;
        for(food_AnalysisInfo f:Lpresent){
            netvL+=f.getKcal()*f.getQuant();
        }
        return netvL;
    }

    public static Double netCalD(){
        netvD=0.0;
        for(food_AnalysisInfo f:Dpresent){
            netvD+=f.getKcal()*f.getQuant();
        }
        return netvD;
    }


    public void setup(){

        ArrayList<String> foodNames=new ArrayList<>();
        foodNames.add(0,"Choose");
        for(FoodInfo foodInfo:foodInfos){
            foodNames.add(foodInfo.getName());
        }ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,foodNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        Breakfast_spinner.setAdapter(adapter);
        Lunch_spinner.setAdapter(adapter);
        Dinner_spinner.setAdapter(adapter);


        aB=new analysisRecyclerAdapter(Bpresent);
        recB.setAdapter(aB);

        aL=new analysisRecyclerAdapter(Lpresent);
        recL.setAdapter(aL);

        aD=new analysisRecyclerAdapter(Dpresent);
        recD.setAdapter(aD);


        netB.setText(String.format("%.3f",netCalB()));
        netL.setText(String.format("%.3f",netCalL()));
        netD.setText(String.format("%.3f",netCalD()));

        note();
    }

    @Override
    public void onStart() {
        super.onStart();
        foodData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodInfos.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    foodInfos.add(snapshot1.getValue(FoodInfo.class));
                    setup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        foodAnalysisB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bpresent=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Bpresent.add(snapshot1.getValue(food_AnalysisInfo.class));
                    Log.d(TAG, "onDataChange: "+Bpresent);
                    setup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        foodAnalysisL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lpresent=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Lpresent.add(snapshot1.getValue(food_AnalysisInfo.class));
                    setup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        foodAnalysisD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Dpresent=new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Dpresent.add(snapshot1.getValue(food_AnalysisInfo.class));
                    setup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}