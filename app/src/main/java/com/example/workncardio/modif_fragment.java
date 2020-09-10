package com.example.workncardio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.databases.FoodInfo;
import com.example.databases.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class modif_fragment extends Fragment {

    private static final String TAG = "";
    TextView age,height,weight,wh;
    Button a,w,h,woh;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");
    UserInfo baseInfo;
    AlertDialog alertDialog;
    String[] hints=new String[]{"Enter Age",
    "Enter Weight",
    "Enter Height",
    "Enter Work Hours"};

    String[] titles=new String[]{"Age","Weight","Height","Work Hours"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_modif_fragment, container, false);
        age=view.findViewById(R.id.update_age);
        height=view.findViewById(R.id.update_height);
        weight=view.findViewById(R.id.update_weight);
        wh=view.findViewById(R.id.update_work);

        a=view.findViewById(R.id.ageBtn);
        w=view.findViewById(R.id.wtBtn);
        h=view.findViewById(R.id.htBtn);
        woh=view.findViewById(R.id.whBtn);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAGe(0);
            }
        });

        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAGe(1);
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAGe(2);
            }
        });

        woh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAGe(3);
            }
        });


        return view;
    }

    public void setVals(){
        age.setText(String.valueOf(baseInfo.getAge()));
        weight.setText(String.valueOf(baseInfo.getWeight()));
        height.setText(String.valueOf(baseInfo.getHeight()));
        wh.setText(String.valueOf(baseInfo.getWorkHour()));
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    UserInfo info=snapshot1.getValue(UserInfo.class);
                    if(info.getName().equals(Homescreen.USER_NAME)){
                        baseInfo=info;
                        Log.d(TAG, "onCreate: "+baseInfo);
                        Log.d(TAG, "onDataChange: called");
                        setVals();



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changeAGe(final int choice ){
        LayoutInflater li = LayoutInflater.from(getContext());
        final View promptsView = li.inflate(R.layout.modify_alert, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());


        alertDialogBuilder.setView(promptsView);
        final EditText foodInput = (EditText) promptsView
                .findViewById(R.id.modifVal);
        foodInput.setHint(hints[choice]);
        final TextView foodItem=promptsView.findViewById(R.id.item1);
        foodItem.setText(titles[choice]);
        final TextView tv=promptsView.findViewById(R.id.unit_modif);
        tv.setText("Set New "+titles[choice]);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Modify",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                switch(choice){
                                    case 0:
                                        baseInfo.setAge(Integer.parseInt(foodInput.getText().toString()));
                                        break;
                                    case 1:
                                        baseInfo.setWeight(Float.parseFloat(foodInput.getText().toString()));
                                        break;
                                    case 2:
                                        baseInfo.setHeight(Float.parseFloat(foodInput.getText().toString()));
                                        break;
                                    case 3:
                                        baseInfo.setWorkHour(Integer.parseInt(foodInput.getText().toString()));
                                        break;
                                }
                                reference.child(baseInfo.getID()).setValue(baseInfo);
                                final ProgressDialog dialog1=new ProgressDialog(getContext());
                                dialog1.setCancelable(false);
                                dialog1.setMessage("Updating");
                                dialog1.create();
                                dialog1.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog1.dismiss();
                                        Toast.makeText(getContext(), "Item Updated", Toast.LENGTH_SHORT).show();
                                    }
                                },2000);

                            }
                        });


        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}