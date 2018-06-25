package com.example.kalyan.timetable;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import java.util.Date;

import static com.example.kalyan.timetable.MainActivity.alarmManager;
import static com.example.kalyan.timetable.MainActivity.pendingIntent;

public class ProjectsActivity extends AppCompatActivity {


    DatePicker datePicker;
    SQLiteDatabase mydatabase;
    EditText subject_proj,project_ed;
    int date,month,year;
    Button button;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        subject_proj = (EditText) findViewById(R.id.subject_proj);
        project_ed = (EditText) findViewById(R.id.assg);
        button = (Button) findViewById(R.id.set_date);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLangDialog1();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                boolean done = save();
                if(done) {
                    item.setIcon(R.drawable.ic_action_name);
                    Intent intent = new Intent(this, ProjectShowActivity.class);
                    startActivity(intent);
                }
                else
                    item.setIcon(R.drawable.ic_notdone);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean save() {
        boolean done = false;
        mydatabase = openOrCreateDatabase("chartDB", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "project(_id INTEGER PRIMARY KEY AUTOINCREMENT,subjects TEXT,projects TEXT ,date TEXT);");

        String subject = subject_proj.getText().toString().trim();
        String project = project_ed.getText().toString().trim();
        if(subject != null && project != null && !subject.contains("null") && !project.contains("null")
                && !subject.equals("") && !project.equals("")) {
            ContentValues values = new ContentValues();
            values.put("subjects", subject);
            values.put("projects", project);
            values.put("date",date+":"+month+":"+year);
            int num = (int)mydatabase.insert("project",null,values);
          //  Toast.makeText(getApplicationContext(),num+"",Toast.LENGTH_LONG).show();
            addProjectNotification(ProjectsActivity.this,date,month);
            done = true;
        }else {
            SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
                    .setText("Enter Valid Data")
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.ANIMATIONS_POP)
                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_INDIGO))
                    .setAnimations(Style.ANIMATIONS_POP).show();
        }
        return done;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void addProjectNotification(Context context,int date,int month ){

        Calendar calendar = Calendar.getInstance();

        // calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.MONTH,month);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        calendar.set(Calendar.DAY_OF_MONTH,date - 1);
        calendar.set(Calendar.HOUR_OF_DAY,prefs.getInt(SettingsActivity.PROJECT_HOUR,15));
        calendar.set(Calendar.MINUTE,prefs.getInt(SettingsActivity.PROJECT_MIN,30));
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(context,Project_Notification_Reciver.class);

        pendingIntent = PendingIntent.getBroadcast(context,
                100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    public void showChangeLangDialog1(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogdate, null);
        dialogBuilder.setView(dialogView);

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        dialogBuilder.setTitle("Select Date");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int whichButton) {
                 date = datePicker.getDayOfMonth();
                 month = datePicker.getMonth();
                 year = datePicker.getYear();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
