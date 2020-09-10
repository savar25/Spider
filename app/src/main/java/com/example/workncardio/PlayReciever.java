package com.example.workncardio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static com.example.workncardio.workoutFragment.mediaPlayer;

public class PlayReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(mediaPlayer!=null){
            mediaPlayer.start();
        }else if(mediaPlayer.isPlaying()){
            Toast.makeText(context, "Already Playing", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Choose a song First ", Toast.LENGTH_SHORT).show();
        }
    }
}
