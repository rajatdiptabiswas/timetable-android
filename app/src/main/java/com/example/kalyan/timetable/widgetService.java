package com.example.kalyan.timetable;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import java.util.Date;

/**
 * Created by KALYAN on 02-10-2017.
 */

public class widgetService extends IntentService {

    public widgetService() {
        super("widgetService");
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ComponentName me= new ComponentName(this,Widgets.class);
        AppWidgetManager mgr= AppWidgetManager.getInstance(this);
        mgr.updateAppWidget(me,buildUpdate(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private RemoteViews buildUpdate(Context context) {
        ComponentName componentName;
        RemoteViews updateViews= new RemoteViews(context.getPackageName(),R.layout.widget);
        componentName = new ComponentName(context, Widgets.class);

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        String day = Utility.Day((currentTime + "").split(" ")[0]);

        //   t.makeText(context,""+context,Toast.LENGTH_LONG).show();
        String retString = "";
        String[] tSQL = context.getResources().getStringArray(R.array.TimeSQL);
        String selectQuery = "SELECT  * FROM " + Contract.Entry.TABLE_NAME + " WHERE "
                + Contract.Entry.COLUMN_DAY + " = " + "\"" + day + "\"";

        Cursor mCursor = (new Helper(context)).getReadableDatabase().rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            if (currentTime.getHours() >= 8 && currentTime.getHours() <= 12) {
                retString = mCursor.getString(mCursor.getColumnIndex(tSQL[currentTime.getHours() - 8]));
            } else if (currentTime.getHours() > 13 && currentTime.getHours() <= 17) {
                retString = mCursor.getString(mCursor.getColumnIndex(tSQL[currentTime.getHours() - 9]));
            } else {
                retString = null;
            }
        }

        if (retString != null &&
                !retString.contains("null") &&
                retString.trim() != "") {
            updateViews.setTextViewText(R.id.widgetTv, "Today is "+day.substring(0,1).toUpperCase()+day.substring(1)+"\n"+"You have "+retString.split("-")[0]+" class at "+retString.split("-")[1]);
        } else {
            updateViews.setTextViewText(R.id.widgetTv, "Today is "+day.substring(0,1).toUpperCase()+day.substring(1)+"\n"+"You don't have any class at the moment");
        }

        updateViews.setOnClickPendingIntent(R.id.widgetTv, getPendingIntent(context));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, updateViews);
        return updateViews;
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, Widgets.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}