package com.example.kalyan.timetable;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Thursday extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    Cursor mCursor;
    final int LOADER_CODE = 4;
    String sqlArray[];
    MyAdapter adapter;
    View view = null;
    ListView list4 = null;
    String tSQL[];
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_thrusday,container,false);
        list4 = (ListView) view.findViewById(R.id.list_view4);
        sqlArray = getResources().getStringArray(R.array.TimeSQL);
        registerForContextMenu(list4);
        Log.e("init","LoADER");
        getActivity().getSupportLoaderManager().initLoader(LOADER_CODE,null,this);
        list4.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(MainActivity.getContext(),EditorActivity.class);
                intent.putExtra("subject",((TextView)view.findViewById(R.id.subject)).getText().toString());
                intent.putExtra("room",((TextView)view.findViewById(R.id.room)).getText().toString());
                intent.putExtra("position",position);
                startActivity(intent);
                return true;
            }
        });
        setView(mCursor);
        return view;
    }


    public void setView(Cursor cursor){
        tSQL = MainActivity.getContext().getResources().getStringArray(R.array.TimeSQL);



        if( cursor !=  null  && cursor.moveToFirst() ) {
            adapter = new MyAdapter(MainActivity.getContext(),cursor,tSQL);
            list4.setAdapter(adapter);
        }

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selectQuery = "SELECT  * FROM " + Contract.Entry.TABLE_NAME + " WHERE "
                + Contract.Entry.COLUMN_DAY + " = " + "\"thursday\"";

        Helper helper = (new Helper(MainActivity.getContext()));
        try {
            mCursor = helper.getReadableDatabase().rawQuery(selectQuery, null);
        }catch (SQLiteException e){

        }

        Log.e("onCreate",""+mCursor);
        //Toast.makeText(getContext(),mCursor+"",Toast.LENGTH_LONG).show();
        return new CursorLoader(getContext(),Contract.Entry.CONTENT_URI,
                null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}

