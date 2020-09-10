package com.example.workncardio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.databases.UserDets;
import com.example.databases.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class initValues extends AppCompatActivity {

    EditText Name,EID,age,weight,height,nom,workH,baseStep;
    Button login;
    UserDets db;
    ImageView imageView;
    Integer logval;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_values);

        logval=getIntent().getIntExtra("log",0);

        imageView=findViewById(R.id.logoForm);
        Name=findViewById(R.id.Fname);
        EID=findViewById(R.id.FID);
        weight=findViewById(R.id.Fweight);
        height=findViewById(R.id.Fheight);
        nom=findViewById(R.id.Fnom);
        workH=findViewById(R.id.FworkHours);
        age=findViewById(R.id.Fage);
        baseStep=findViewById(R.id.Fsteps);
        login=findViewById(R.id.loginBtn);
        db=new UserDets(this);

        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        EID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });

        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        workH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        nom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        baseStep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkVals()) {
                    Float weight1 = Float.parseFloat(weight.getText().toString());
                    Float height1 = Float.parseFloat(height.getText().toString());
                    Double BMI = weight1 / Math.pow(height1, 2);

                    String id=reference.push().getKey();
                    UserInfo temp = new UserInfo(id,Name.getText().toString(), EID.getText().toString(), Integer.parseInt(age.getText().toString()), Integer.parseInt(workH.getText().toString()), Integer.parseInt(nom.getText().toString()), Integer.parseInt(baseStep.getText().toString()), Float.parseFloat(weight.getText().toString()), Float.parseFloat(height.getText().toString()), BMI,4,false);
                    reference.child(id).setValue(temp);
                    Intent intent = new Intent(initValues.this,Homescreen.class);
                    intent.putExtra("Name1",Name.getText().toString());
                    intent.putExtra("fragment","def");
                    intent.setAction("");
                    startActivity(intent);
                }

            }
        });


    }

    public boolean checkVals(){
        if(Name.toString().isEmpty()){
            Toast.makeText(this, "Name Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(EID.toString().isEmpty()){
            Toast.makeText(this, "E-Mail Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(weight.toString().isEmpty()){
            Toast.makeText(this, "Weight Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(height.toString().isEmpty()){
            Toast.makeText(this, "Height Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(workH.toString().isEmpty()){
            Toast.makeText(this, "Work Hours Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(nom.toString().isEmpty()){
            Toast.makeText(this, "Number of Meals Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(age.toString().isEmpty()){
            Toast.makeText(this, "Age Absent", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(baseStep.toString().isEmpty()){
            Toast.makeText(this, "Base Steps Absent", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}