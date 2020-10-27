package com.example.kalyan.timetable;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.github.clans.fab.FloatingActionButton;

import static com.example.kalyan.timetable.MainActivity.getContext;

public class ProjectShowActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    final int LOADER_CODE_PROJ = 2;
    SQLiteDatabase mydatabase;
    FloatingActionButton fab = null;
    ListView listView;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_show);

        this.getSupportLoaderManager().initLoader(LOADER_CODE_PROJ, null, this);

        fab = (FloatingActionButton) findViewById(R.id.fab_proj);
        listView = (ListView) findViewById(R.id.proj_list_view);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProjectShowActivity.this, ProjectsActivity.class);
                startActivity(intent);
            }
        });

        if (mCursor != null)
        {
            MyProjAdapter adapter = new MyProjAdapter(this, mCursor, 0);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {

        String selectQuery = "SELECT  * FROM " + "project ;";

        try
        {
            mydatabase = openOrCreateDatabase("chartDB", MODE_PRIVATE, null);
            mCursor = mydatabase.rawQuery(selectQuery, null);
        }
        catch (SQLiteException e)
        {

        }

        return new CursorLoader(getContext(), Contract.Entry.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        // Toast.makeText(getContext(), "onLoadfinish   proj", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

        // Toast.makeText(getContext(), "onloaderreset   proj", Toast.LENGTH_SHORT).show();
    }

    private class MyProjAdapter extends CursorAdapter
    {
        String tempSt;
        Cursor cursor;
        LayoutInflater cursorInflater;


        public MyProjAdapter(@NonNull Context context, Cursor cursor, int flag)
        {
            super(context, cursor, flag);
            this.cursor = cursor;
            cursorInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            return cursorInflater.inflate(R.layout.single_proj, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            TextView datetv = (TextView) view.findViewById(R.id.list_single_date);
            TextView subtv = (TextView) view.findViewById(R.id.subject_proj);
            TextView projtv = (TextView) view.findViewById(R.id.proj_tv);

            datetv.setText(cursor.getString(cursor.getColumnIndex("date")));
            subtv.setText(cursor.getString(cursor.getColumnIndex("subjects")));
            projtv.setText(cursor.getString(cursor.getColumnIndex("projects")));

            //   Toast.makeText(getContext(), "bind view", Toast.LENGTH_SHORT).show();


        }

    }
}
