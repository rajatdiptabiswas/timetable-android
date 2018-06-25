package com.example.kalyan.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by KALYAN on 26-09-2017.
 */

public class Utility {
    static Cursor mCursor;
    static String tSQL[];

    static int daysFirstClassTime(String day){

        int firstClassTime = 0;
        tSQL = MainActivity.getContext().getResources().getStringArray(R.array.TimeSQL);
        String selectQuery = "SELECT  * FROM " + Contract.Entry.TABLE_NAME + " WHERE "
                + Contract.Entry.COLUMN_DAY + " = " + "\""+day+"\"";

        mCursor = (new Helper(MainActivity.getContext())).getReadableDatabase().rawQuery(selectQuery, null);

        int position = 0;
        if(mCursor.moveToFirst()){
            String tempSt;
            do {
                tempSt = mCursor.getString(mCursor.getColumnIndex(tSQL[position]));
                if(tempSt != null && !tempSt.equals("") && !tempSt.equals("null") && !tempSt.contains("null")){
                    firstClassTime = Integer.parseInt(number((tSQL[position].split("to"))[0]).split(" ")[0]);
                    break;
                }
                position++;
            }while (position != mCursor.getColumnCount()-2);
        }
        return firstClassTime;
    }

    static int daysLastClassTime(String  day){
        int LastClassTime = 0;
        tSQL = MainActivity.getContext().getResources().getStringArray(R.array.TimeSQL);
        String selectQuery = "SELECT  * FROM " + Contract.Entry.TABLE_NAME + " WHERE "
                + Contract.Entry.COLUMN_DAY + " = " + "\""+day+"\"";

        mCursor = (new Helper(MainActivity.getContext())).getReadableDatabase().rawQuery(selectQuery, null);

        Collections.reverse(Arrays.asList(tSQL));
        int position = 0;
        if(mCursor.moveToFirst()){
            String tempSt;
            do {
                tempSt = mCursor.getString(mCursor.getColumnIndex(tSQL[position]));
                if(tempSt != null && !tempSt.equals("") && !tempSt.equals("null") && !tempSt.contains("null")){
                    LastClassTime = Integer.parseInt(number((tSQL[position].split("to"))[0]).split(" ")[0]);
                    break;
                }
                position++;
            }while (position != mCursor.getColumnCount()-2);
        }
        return LastClassTime;

    }
    static void nullAll(){
        tSQL = MainActivity.getContext().getResources().getStringArray(R.array.TimeSQL);
        String selectQuery = "SELECT  * FROM " + Contract.Entry.TABLE_NAME ;
        mCursor = (new Helper(MainActivity.getContext())).getReadableDatabase().rawQuery(selectQuery, null);

        if(mCursor.moveToFirst() && mCursor != null){

          do{
              String tempSt;
              int position = 0;
              String d = mCursor.getString(mCursor.getColumnIndex("day"));
              String day[] = {d};
              do {
                  tempSt = mCursor.getString(mCursor.getColumnIndex(tSQL[position]));
                  if(tempSt != null && !tempSt.equals("") && !tempSt.equals("null") && !tempSt.contains("null")){
                      ContentValues values = new ContentValues();
                      values.put(tSQL[position],"null");
                      MainActivity.getContext().getContentResolver().update(Contract.Entry.CONTENT_URI, values, "day = ?",day);
                  }
                  position++;
              }while (position != 9);

          }while (mCursor.moveToNext());
        }


    }
    static String Day(String day){
        if(day.equals("Mon"))
            return "monday";
        else if(day.equals("Tue"))
            return "tuesday";
        else if(day.equals("Wed"))
            return "wednesday";
        else if(day.equals("Thu"))
            return "thursday";
        else if(day.equals("Fri"))
            return "friday";
        else if(day.equals("Sat"))
            return "saturday";
        else if(day.equals("Sun"))
            return "sunday";
        else
            return null;
    }

    static String  number(String num){
        if(num.equals("one"))
            return "1 PM";
        else if(num.equals("two"))
            return "2 PM";
        else if(num.equals("three"))
            return "3 PM";
        else if(num.equals("four"))
            return "4 PM";
        else if(num.equals("five"))
            return "5 PM";
        else if(num.equals("six"))
            return "6 PM";
        else if(num.equals("seven"))
            return "7 AM";
        else if(num.equals("eight"))
            return "8 AM";
        else if(num.equals("nine"))
            return "9 AM";
        else if(num.equals("ten"))
            return "10 AM";
        else if(num.equals("eleven"))
            return "11 AM";
        else if(num.equals("twelve"))
            return "12 NOON";
        else
            return null;
    }


     public static String getResults(Cursor cur) {
        String searchQuery = "SELECT  * FROM " + "subject";
        Cursor cursor = cur;// new Helper(MainActivity.getContext()).getReadableDatabase().rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "null");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
       // cursor.close();
        Log.e("TAG_NAME", resultSet.toString());
        return resultSet.toString();
    }



    public static void Save(String data,Context context)
    {
        String path = "/sdcard/mytxd.txt";
        try
        {
            File file = new File(path);
            file.delete();
            if(!file.exists()){
              //  Toast.makeText(context,"New file Created", Toast.LENGTH_LONG).show();
                file.createNewFile();
                Log.i("filepath", file.getAbsolutePath().toString());
            }

            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();

        }
        catch (FileNotFoundException e)
        {e.printStackTrace();}
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String Load(Context context){
        String retSt = "";

        BufferedReader br = null;
        String path = "/sdcard/mytxt.txt";
        try {
            br = new BufferedReader(new FileReader(path));
            String line = "";
            while((line = br.readLine())!= null){
                retSt = retSt + line;
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"File not found", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  retSt;
    }



}




