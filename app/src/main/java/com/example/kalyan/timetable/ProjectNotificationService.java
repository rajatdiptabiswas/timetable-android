package com.example.kalyan.timetable;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

/**
 * Created by KALYAN on 08-10-2017.
 */

class ProjectNotificationService extends IntentService{

    private static NotificationManager manager;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ProjectNotificationService(String name) {
        super("projectnotificationservice");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = this;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context,ProjectsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle("TimeTable");

        Calendar calendar = Calendar.getInstance();

        Date currentTime = calendar.getTime();

        String day = currentTime.getMonth() +":" +currentTime.getDate() +":" + currentTime.getYear();

        String selectQuery = "SELECT  * FROM " + "project" + " WHERE"
                + " date" + " = " + "\""+day+"\"";

        SQLiteDatabase mydatabase = openOrCreateDatabase("chartDB", MODE_PRIVATE, null);

        Cursor cursor = mydatabase.rawQuery(selectQuery, null);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(cursor.moveToFirst() && prefs.getBoolean(SettingsActivity.SHOW_NOTIFICATION, false) ){
            String tempSt;
            String sub = cursor.getString(cursor.getColumnIndex("subjects"));
            String proj = cursor.getString(cursor.getColumnIndex("projects"));

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher2)
                            .setTicker("TimeTable Notification")
                            .setContentTitle("Todays TimeTable")
                            .setContentText("You have to submit projects on "+sub+"on "+proj+" topic.")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setStyle(style);

            manager.notify(101, builder.build());//the last 2line is must to show the notification and get system service
        }
    }
}
