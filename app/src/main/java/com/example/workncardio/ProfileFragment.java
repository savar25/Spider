package com.example.workncardio;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ProfileFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    FragmentTransaction profFragmentTransaction;
    FragmentManager profFragmentManager;
    NavigListener listener;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view=inflater.inflate(R.layout.fragment_profile, container, false);


        bottomNavigationView=view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                listener.onItemSelected(item.getItemId());
                return true;
            }
        });



        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof NavigListener){
            listener=(NavigListener)context;
        }else{
            throw new ClassCastException("Error");
        }
    }

    public interface NavigListener{
        public void onItemSelected(int ID);
    }
}