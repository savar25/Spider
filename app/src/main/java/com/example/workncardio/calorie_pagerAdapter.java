package com.example.workncardio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class calorie_pagerAdapter extends FragmentStatePagerAdapter {
    int TabNumber;

    public calorie_pagerAdapter(@NonNull FragmentManager fm, int behavior, int tabNumber) {
        super(fm, behavior);
        TabNumber = tabNumber;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new calorie_foodFragment();
            case 1:
                return new calCalc();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence sequence="";
        switch(position){
            case 0: sequence="Calorie List";
            break;
            case 1:sequence="Calorie Calculator";
            break;

        }
        return sequence;
    }

    @Override
    public int getCount() {
        return TabNumber;
    }
}
