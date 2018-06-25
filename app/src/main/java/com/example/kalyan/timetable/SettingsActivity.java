package com.example.kalyan.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.kalyan.timetable.MainActivity.getContext;

public class SettingsActivity extends AppCompatActivity{

    LinearLayout reset,project_time,notify_time;
    Switch aSwitch;
    final static String PROJECT_HOUR = "hour",PROJECT_MIN = "min";
    final static String NOT_HOUR = "hour1",NOT_MIN = "min1";
    SharedPreferences pref ;
    final public static String  SHOW_NOTIFICATION  = "show notification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        project_time = (LinearLayout) findViewById(R.id.set_proj_notification);
         notify_time = (LinearLayout) findViewById(R.id.set_notification);

        aSwitch = (Switch) findViewById(R.id.switch2);
        aSwitch.setChecked(pref.getBoolean(SHOW_NOTIFICATION, false));
        //Toast.makeText(getContext(),pref.getBoolean(SHOW_NOTIFICATION, false)+"",Toast.LENGTH_LONG).show();
        reset = (LinearLayout) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.nullAll();
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(SHOW_NOTIFICATION,isChecked);
                editor.apply();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int hour = prefs.getInt(PROJECT_HOUR,12);
        int min = prefs.getInt(PROJECT_MIN,0);

        ((TextView)findViewById(R.id.proj_notification_time)).setText("You will receive notification at "+hour+":"+min);

         hour = prefs.getInt(NOT_HOUR,12);
         min = prefs.getInt(NOT_MIN,0);
        ((TextView)findViewById(R.id.notification_time)).setText("You will receive notification at "+hour+":"+min);
        project_time.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showChangeLangDialog1();
            }
        });
        notify_time.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showChangeLangDialog2();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showChangeLangDialog1() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogtime, null);
        dialogBuilder.setView(dialogView);

        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        timePicker.setCurrentMinute(min);
        timePicker.setCurrentHour(hour);

        dialogBuilder.setTitle("Select Time");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                ((TextView)findViewById(R.id.proj_notification_time)).setText("You will receive notification at "+hour+":"+min);
                saveTime(hour,min);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void saveTime(int hour,int min){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PROJECT_HOUR,hour);
        editor.putInt(PROJECT_MIN,min);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showChangeLangDialog2() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogtime, null);
        dialogBuilder.setView(dialogView);

        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        timePicker.setCurrentMinute(min);
        timePicker.setCurrentHour(hour);

        dialogBuilder.setTitle("Select Time");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                ((TextView)findViewById(R.id.notification_time)).setText("You will receive notification at "+hour+":"+min);
                saveTime2(hour,min);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void saveTime2(int hour,int min){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NOT_HOUR,hour);
        editor.putInt(NOT_MIN,min);
        editor.apply();
    }
}
