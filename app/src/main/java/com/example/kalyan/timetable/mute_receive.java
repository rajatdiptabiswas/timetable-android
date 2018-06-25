package com.example.kalyan.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by KALYAN on 12-10-2017.
 */

public class mute_receive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        generalMode(context);
    }

    private void generalMode(Context context){
       AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

    }
}
