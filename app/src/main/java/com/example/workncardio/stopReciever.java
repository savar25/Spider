package com.example.workncardio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class stopReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        workoutFragment.mediaPlayer.stop();
    }
}
