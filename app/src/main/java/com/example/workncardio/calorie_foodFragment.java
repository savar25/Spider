package com.example.workncardio;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.databases.FoodDatabase;
import com.example.databases.FoodInfo;
import com.example.databases.food_AnalysisInfo;
import com.example.databases.food_analysisDatabase;
import com.example.workncardio.dummy.FoodContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class calorie_foodFragment extends Fragment implements MyFoodRecyclerViewAdapter.LongClickListener{

    RecyclerView recyclerView;
    MyFoodRecyclerViewAdapter foodRecyclerViewAdapter;
    ArrayList<FoodInfo> foodInfos=new ArrayList<>();
    FoodDatabase foodDatabase;
    FloatingActionButton fab;
    public View view;
    food_analysisDatabase food_analysisDatabaseB,food_analysisDatabaseL,food_analysisDatabaseD;
    private static final String TAG = "calorie_foodFragment";
    modif backtoOriginal;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference foodData=firebaseDatabase.getReference("Food Data").child(Homescreen.USER_NAME);
    DatabaseReference foodAnalysisB=firebaseDatabase.getReference("Food Analysis BreakFast").child(Homescreen.USER_NAME);
    DatabaseReference foodAnalysisL=firebaseDatabase.getReference("Food Analysis Lunch").child(Homescreen.USER_NAME);
    DatabaseReference foodAnalysisD=firebaseDatabase.getReference("Food Analysis Dinner").child(Homescreen.USER_NAME);
    ArrayList<FoodInfo> foodInfoArrayList;
    Context context;
    boolean flag;
    Integer val;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calorie_food_list, container, false);
        fab=view.findViewById(R.id.fab);

        context = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.foodRec);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater li = LayoutInflater.from(getContext());
                final View promptsView = li.inflate(R.layout.alert_dialogue_input, null);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                alertDialogBuilder.setView(promptsView);

                final EditText foodInput = (EditText) promptsView
                        .findViewById(R.id.foodval);
                final EditText calInput=promptsView.findViewById(R.id.calval);
                final View view1=getView();
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("ADD",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String id1=foodData.push().getKey();
                                        foodData.child(id1).setValue(new FoodInfo(id1,foodInput.getText().toString(),Double.valueOf(calInput.getText().toString())));
                                        Snackbar.make(view1,"Food Item Added",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                                   }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }

                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }

        });
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
        return view;
    }




    ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            FoodInfo foodInfo=foodInfoArrayList.get(viewHolder.getAdapterPosition());
            foodData.child(foodInfo.getId()).removeValue();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.Grey1,null))
                    .addSwipeRightLabel("Remove")
                    .addSwipeRightActionIcon(android.R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };


    @Override
    public void onLongClickListener(int position) {
        final FoodInfo foodInfo=foodInfoArrayList.get(position);
        Toast.makeText(getContext(),"A: "+foodInfoArrayList.get(position).getName(),Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.modify_alert, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder.setView(promptsView);

        final EditText foodInput = (EditText) promptsView
                .findViewById(R.id.modifVal);
        final TextView foodItem=promptsView.findViewById(R.id.item1);
        foodItem.setText(foodInfo.getName());
        final View view1 = getView();
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Modify",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                foodData.child(foodInfo.getId()).setValue(new FoodInfo(foodInfo.getId(),foodInfo.getName(),Double.parseDouble(foodInput.getText().toString())));
                                checkAnalysisB(foodInfo.getId(),Double.parseDouble(foodInput.getText().toString()));
                                checkL(foodInfo.getId(),Double.parseDouble(foodInput.getText().toString()));
                                checkD(foodInfo.getId(),Double.parseDouble(foodInput.getText().toString()));
                                backtoOriginal.onModify();

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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof modif) {
            backtoOriginal = (modif) context;
        } else {
            throw new ClassCastException("Error");
        }
    }

    public interface modif{
        void onModify();
    }

    public void init(){
        if(foodInfoArrayList.isEmpty()){
            String id=foodData.push().getKey();
            foodData.child(id).setValue(new FoodInfo(id,"Bread",0.2));
        }

        foodRecyclerViewAdapter=new MyFoodRecyclerViewAdapter(foodInfoArrayList,this);
        recyclerView.setAdapter(foodRecyclerViewAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        foodData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodInfoArrayList=new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren()){
                    foodInfoArrayList.add(snap.getValue(FoodInfo.class));
                }
                init();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public boolean checkAnalysisB(final String id, final Double cal){
        flag=false;
        foodAnalysisB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.getKey().equals(id)){
                        food_AnalysisInfo foodInfo=snapshot1.getValue(food_AnalysisInfo.class);
                        foodInfo.setKcal(cal);
                        getQuant(foodInfo,1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return flag;
    }

    public boolean checkL(final String id,final Double cal){
        flag=false;
        foodAnalysisL.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.getKey().equals(id)){
                        food_AnalysisInfo foodInfo=snapshot1.getValue(food_AnalysisInfo.class);
                        foodInfo.setKcal(cal);
                        getQuant(foodInfo,2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return flag;
    }

    public boolean checkD(final String id,final Double cal){
        flag=false;
        foodAnalysisD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.getKey().equals(id)){
                        food_AnalysisInfo foodInfo=snapshot1.getValue(food_AnalysisInfo.class);
                        foodInfo.setKcal(cal);
                        getQuant(foodInfo,3
                        );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d(TAG, "checkD: "+flag);
        return flag;
    }

    public boolean getQuant(final food_AnalysisInfo foodInfo, final int val){

        DatabaseReference reference=null;
        switch(val){
            case 1:
                reference=foodAnalysisB;
                break;
            case 2:
                reference=foodAnalysisL;
                break;

            case 3:
                reference=foodAnalysisD;
                break;
        }

        reference.child(foodInfo.getId()).setValue(foodInfo);
        return true;
    }


}