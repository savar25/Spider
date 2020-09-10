package com.example.workncardio;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class cardio_cal_fragment extends Fragment {

    RadioButton walk,cycle,run,cross;
    Integer setup;
    ButtonListener buttonListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cardio_cal_fragment, container, false);

        walk=view.findViewById(R.id.rdbWalk);
        cycle=view.findViewById(R.id.rdbCycle);
        run=view.findViewById(R.id.rdbRun);
        cross=view.findViewById(R.id.rdbCrossTrainer);
        final EditText editText=view.findViewById(R.id.editTextTextPersonName);
        if(walk.isChecked()){
            setup=0;
        }else if(cycle.isChecked()){
            setup=1;
        }else if(run.isChecked()){
            setup=2;
        }else if(cross.isChecked()){
            setup=3;
        }

        TextView tv=view.findViewById(R.id.goBtn);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty())
                {
                    buttonListener.onPress(setup,Double.parseDouble(editText.getText().toString()));
                }else {
                    Toast.makeText(getContext(), "Enter Calories", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ButtonListener){
            buttonListener=(ButtonListener) context;
        }else{
            throw new ClassCastException("Error");
        }
    }

    public interface ButtonListener{
        public void onPress(int choice,Double kcal);
    }

}
