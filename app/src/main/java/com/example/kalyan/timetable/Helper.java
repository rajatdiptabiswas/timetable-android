package com.example.kalyan.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KALYAN on 26-08-2017.
 */

public class Helper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "timetable.db";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREAT_TABLE = "CREATE TABLE " + Contract.Entry.TABLE_NAME + "("
                + Contract.Entry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                Contract.Entry.COLUMN_DAY + " TEXT ," +
                Contract.Entry.COLUMN_8to9 + " TEXT ," +
                Contract.Entry.COLUMN_9to10 + " TEXT ," +
                Contract.Entry.COLUMN_10to11 + " TEXT ," +
                Contract.Entry.COLUMN_11to12 + " TEXT ," +
                Contract.Entry.COLUMN_12to1 + " TEXT ," +
                Contract.Entry.COLUMN_2to3 + " TEXT ," +
                Contract.Entry.COLUMN_3to4 + " TEXT ," +
                Contract.Entry.COLUMN_4to5 + " TEXT ,"+
                Contract.Entry.COLUMN_5to6 + " TEXT" + ");";

        db.execSQL(SQL_CREAT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
