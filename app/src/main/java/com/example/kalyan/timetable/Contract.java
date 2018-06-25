package com.example.kalyan.timetable;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by KALYAN on 26-08-2017.
 */

public class Contract {

    private Contract() {
    }

    /* Inner class that defines the table contents */
    public static class Entry implements BaseColumns {/*here we are writing the single table becoz
    there is only one table i.e pets table  so.... */

        public static final String AUTHORITY = "com.example.kalyan.timetable";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        public static final String PATH = "chartDB";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH)
                .build();


        public static final String TABLE_NAME = "chart";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_8to9 = "eighttonine";
        public static final String COLUMN_9to10 = "ninetoten";
        public static final String COLUMN_10to11 = "tentoeleven";
        public static final String COLUMN_11to12 = "eleventotwelve";
        public static final String COLUMN_12to1 = "twelvetoone";
        public static final String COLUMN_2to3 = "twotothree";

        public static final String COLUMN_3to4 = "threetofour";
        public static final String COLUMN_4to5 = "fourtofive";
        public static final String COLUMN_5to6 = "fivetosix";
    }
}
