package com.example.kalyan.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class AttendenceActivity extends AppCompatActivity {

    Cursor mCursor;
    ListView listView;
    SQLiteDatabase mysubdatabase = null;
    static String tSQL[];
    MyattAdapter adapter;

    static Hashtable<String,String> databaseData = new Hashtable<>();
    static Hashtable<String,String> edittedData = new Hashtable<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        Calendar c = Calendar.getInstance();

        String day = Utility.Day(c.getTime().toString().split(" ")[0]);

        Log.e("day",day);
        listView = (ListView) findViewById(R.id.attendance_list);
        String selectQuery = "SELECT * FROM " + Contract.Entry.TABLE_NAME + " WHERE "
                + Contract.Entry.COLUMN_DAY + " = " + "\""+day+"\"";

        Helper helper = (new Helper(MainActivity.getContext()));
        try {
            mCursor = helper.getReadableDatabase().rawQuery(selectQuery, null);
        }catch (SQLiteException e){

        }


        tSQL = MainActivity.getContext().getResources().getStringArray(R.array.TimeSQL);
        int position = 0;
        ArrayList<String> titles = new ArrayList<>();
        if(mCursor.moveToFirst()){
            String tempSt;
            do {
                tempSt = mCursor.getString(mCursor.getColumnIndex(tSQL[position]));

                if(tempSt != null && !tempSt.equals("") && !tempSt.equals("null") && !tempSt.contains("null")){
                    Log.e("tempSt",tempSt);
                    titles.add(tempSt.split("-")[0]);
                }
                position++;
            }while (position != mCursor.getColumnCount()-2);
        }
        if(titles.size()>0) {
            Log.e("titleSize",titles.size()+"");
            double[] percentage = new double[titles.size()];

            for(int i = 0;i <titles.size();i++){
                mysubdatabase = openOrCreateDatabase("DB", MODE_PRIVATE, null);
                mysubdatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                        "subject(_id INTEGER PRIMARY KEY AUTOINCREMENT,subjects TEXT,attended INTEGER DEFAULT 0,total INTEGER DEFAULT 0);");

                String query = "SELECT * FROM subject WHERE subjects = " + "\""+titles.get(i)+ "\"";
                String  key = titles.get(i);
                Cursor cursor = mysubdatabase.rawQuery(query, null);

                try {
                    cursor.moveToFirst();
                    String val=cursor.getInt(2)+",";
                    percentage[i] = cursor.getInt(2)*1.0;
                    Log.e("attended",percentage[i]+"");

                    int tot=cursor.getInt(3);
                    val=val+tot;
                    databaseData.put(key,val);
                    if (tot==0)
                        tot=1;
                    percentage[i] /= tot;
                    Log.e("total",percentage[i]+"");
                    percentage[i]*=100;
                }
                catch (Exception e) {
                    Log.e("Exception caught",e.toString());
                    cursor.close();
                }

            }

            Log.e("HashTable ",databaseData.toString());
             adapter = new MyattAdapter(this, titles.toArray(new String[titles.size()]),percentage,databaseData);
            listView.setAdapter(adapter);
        }

    }

    private void savetoDatabase() {
        if (adapter==null)
            return;
        edittedData=adapter.getHashTable();
        databaseData=edittedData;
        Log.e("edittedData",edittedData.toString());
        mysubdatabase = openOrCreateDatabase("DB", MODE_PRIVATE, null);
        mysubdatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                "subject(_id INTEGER PRIMARY KEY AUTOINCREMENT,subjects TEXT,attended INTEGER DEFAULT 0,total INTEGER DEFAULT 0);");



        Set<String> keys = edittedData.keySet();
        for (String key:keys) {
            //String updateQuery1 = "UPDATE subject SET attended = "+edittedData.get(key).split(",")[0]+" WHERE subjects = \"" + key+"\";";
            //String updateQuery2 = "UPDATE subject SET total = "+edittedData.get(key).split(",")[1]+" WHERE subjects = \"" + key+"\";";

            ContentValues values = new ContentValues();
            values.put("attended",edittedData.get(key).split(",")[0]);
            values.put("total",edittedData.get(key).split(",")[1]);
            mysubdatabase.update("subject",values,"subjects = ?",new String[]{key});
            //mysubdatabase.rawQuery(updateQuery2,null);
        }
        finish();
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
                savetoDatabase();
        }
        return super.onOptionsItemSelected(item);
    }

}

 class MyattAdapter extends BaseAdapter {

    private Context context;
    private String[] titles;
    private int[] images;
    private String[] percentage;
    private LayoutInflater inflater;
    Hashtable<String,Boolean> changed;
    Hashtable<String,String> databaseData;

    public MyattAdapter(Context context, String[] titles,double[] percentage,Hashtable<String,String> datab) {
        this.context = context;
        this.databaseData=datab;
        this.titles = titles;
        changed=new Hashtable<>();
        for (int i=0;i<this.titles.length;i++) {
            changed.put(this.titles[i],false);
        }
        this.percentage=new String[percentage.length];
        if (percentage==null) {
            Log.e("Error ","null array");
        }
        else {
            for (int i=0;i<percentage.length;i++) {
                this.percentage[i] = String.format("%.1f",(float)percentage[i]);
            }
        }


        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.single_check, null);
            mViewHolder = new ViewHolder();
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tvTitle = (CheckBox) convertView
                .findViewById(R.id.checkBox);
        mViewHolder.per_tv = (TextView)convertView.findViewById(R.id.percentage_id);
        mViewHolder.tvTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String  key=mViewHolder.tvTitle.getText().toString();
                    int attended=Integer.valueOf((databaseData.get(key)).split(",")[0]);
                    int total=Integer.valueOf((databaseData.get(key)).split(",")[1]);
                    attended++;
                    total++;
                    String val=attended+","+total;
                    changed.put(key,true);
                    databaseData.put(key,val);
                    percentage[position]=(attended*100.0/total)+"";
                    mViewHolder.per_tv.setText(percentage[position]);
                }
                else {
                    String  key=mViewHolder.tvTitle.getText().toString();
                    int attended=Integer.valueOf((databaseData.get(key)).split(",")[0]);
                    int total=Integer.valueOf((databaseData.get(key)).split(",")[1]);
                    attended--;
                    total--;
                    changed.put(key,false);
                    String val=attended+","+total;
                    databaseData.put(key,val);
                    if (total==0)
                        total=1;
                    percentage[position]=(attended*100.0/total)+"";
                    mViewHolder.per_tv.setText(percentage[position]);
                }
            }
        });


        mViewHolder.tvTitle.setText(titles[position]);
        mViewHolder.per_tv.setText(percentage[position]);


        return convertView;
    }

    public Hashtable<String,String> getHashTable() {
        Set<String> keys=changed.keySet();
        for (String key:keys) {
            if (!changed.get(key)) {
                databaseData.put(key,databaseData.get(key).split(",")[0]+","+(Integer.valueOf(databaseData.get(key).split(",")[1])+1));
            }
        }
        return databaseData;
    }

    private class ViewHolder {
        CheckBox tvTitle;
        TextView per_tv;
    }
}


