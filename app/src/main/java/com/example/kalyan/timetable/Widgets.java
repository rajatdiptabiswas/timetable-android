package com.example.kalyan.timetable;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by KALYAN on 01-10-2017.
 */

public class Widgets extends AppWidgetProvider
{

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        context.startService(new Intent(context, widgetService.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        context.startService(new Intent(context, widgetService.class));
    }
}

