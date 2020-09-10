package com.example.workncardio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class goalChange extends Fragment {


    TextView current,expected;
    EditText newVal;
    ImageView tick;
    UserDets dets;
    UserInfo baseInfo;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_goal_change, container, false);
        current=view.findViewById(R.id.currentGoal);
        expected=view.findViewById(R.id.ExpectedGoal);
        newVal=view.findViewById(R.id.setnewGoal);
        tick=view.findViewById(R.id.setGoalBtn);





        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Change entry")
                        .setMessage("Are you sure you want to change this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                changeBase();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
            }
        });



        return view;
    }

    public void changeBase(){

        Integer val=Integer.parseInt(newVal.getText().toString());
        baseInfo.setBaseStepGoal(val);
        reference.child(baseInfo.getID()).setValue(baseInfo);
        current.setText(String.valueOf(baseInfo.getBaseStepGoal()));
        final ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setMessage("Updating...");
        dialog.create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },2000);

    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    UserInfo info=snapshot1.getValue(UserInfo.class);
                    if(info.getName().equals(Homescreen.USER_NAME)){
                        baseInfo=info;
                        init();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void init(){
        current.setText(String.valueOf(baseInfo.getBaseStepGoal()));
        if(baseInfo.getBMI()>=26){
            if(baseInfo.getWeight()<60){
                expected.setText(String.valueOf(7000));
            }else{
                expected.setText(String.valueOf(10000));
            }
        }else {
            if(baseInfo.getWeight()<50){
                expected.setText(String.valueOf(4000));
            }else{
                expected.setText(String.valueOf(8000));
            }
        }
    }
}

