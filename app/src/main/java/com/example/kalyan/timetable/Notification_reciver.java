package com.example.kalyan.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by KALYAN on 26-09-2017.
 */

public class Notification_reciver extends BroadcastReceiver
{

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.startService(new Intent(context, NotificationService.class));
    }

}