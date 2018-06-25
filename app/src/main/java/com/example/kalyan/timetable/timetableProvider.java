package com.example.kalyan.timetable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by KALYAN on 26-08-2017.
 */

public class timetableProvider extends ContentProvider {

    final static int CODE_TABLE = 100;
    final static int CODE_TABLE_WITH_DAY = 101;

    private Helper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new Helper(getContext());
        return true;
    }

    static UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(Contract.Entry.AUTHORITY, Contract.Entry.PATH,CODE_TABLE);
        matcher.addURI(Contract.Entry.AUTHORITY, Contract.Entry.PATH+"/#",CODE_TABLE_WITH_DAY);

        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase rDb = mOpenHelper.getReadableDatabase();
        Cursor retCursor;
        switch (match){
            case CODE_TABLE:
                retCursor = rDb.query(Contract.Entry.TABLE_NAME,projection
                        ,selection,selectionArgs,null,null,sortOrder);
                break;
            case CODE_TABLE_WITH_DAY:
                String DateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{DateString};
                retCursor = rDb.query(Contract.Entry.TABLE_NAME,projection,
                        Contract.Entry.COLUMN_DAY+" = ?",selectionArgs,null,null,sortOrder);
                break;
            default:
                throw  new RuntimeException("this is a undefined URI");
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase wDb = mOpenHelper.getWritableDatabase();
        long rowInserted = -1;
        Uri retUri ;
        switch (match) {
            case CODE_TABLE:

                rowInserted = wDb.insert(Contract.Entry.TABLE_NAME,null,values);
                retUri = Uri.withAppendedPath(uri,rowInserted+"");
                break;
            default:
                throw  new RuntimeException("this is a undefined URI");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase wDb = mOpenHelper.getWritableDatabase();
        int rowUpdated = -1;
        Uri retUri;
        switch (match){
            case CODE_TABLE:
                rowUpdated = wDb.update(Contract.Entry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw  new RuntimeException("this is a undefined URI");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowUpdated;

    }

}
