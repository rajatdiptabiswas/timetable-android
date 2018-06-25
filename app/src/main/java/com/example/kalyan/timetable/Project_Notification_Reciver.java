package com.example.kalyan.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by KALYAN on 08-10-2017.
 */

class Project_Notification_Reciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ProjectNotificationService.class));
    }
}
