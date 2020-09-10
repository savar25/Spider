package com.example.workncardio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class version extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_version, container, false);
    }

    @Override
    public void onDestroy() {

        Intent intent=new Intent(getContext(),Homescreen.class);
        intent.putExtra("fragment","def");
        intent.putExtra("Name1",Homescreen.USER_NAME);
        intent.setAction("");
        startActivity(intent);

        super.onDestroy();
    }

    @Override
    public void onPause() {

        Intent intent=new Intent(getContext(),Homescreen.class);
        intent.putExtra("fragment","def");
        intent.putExtra("Name1",Homescreen.USER_NAME);
        intent.setAction("");
        startActivity(intent);
        super.onPause();
    }
}