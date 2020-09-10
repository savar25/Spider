package com.example.workncardio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PauseReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        workoutFragment.mediaPlayer.pause();
    }
}
