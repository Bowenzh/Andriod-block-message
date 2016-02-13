package com.jikexueyuan.blockmsg;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NumberActivity extends ListActivity {


    private SimpleCursorAdapter adapter;
    private Button btnAddNumber;
    private DatabaseNumber databaseNumber;
    private SQLiteDatabase dbRead,dbWrite;
    private EditText etAddNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        btnAddNumber = (Button) findViewById(R.id.btnAddNumber);


        btnAddNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(NumberActivity.this, R.layout.number_input,null);
                etAddNumber = (EditText) view.findViewById(R.id.etAddNumber);
                new AlertDialog.Builder(NumberActivity.this)
                        .setTitle("Please to enter a number")
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues cv = new ContentValues();
                                String s = etAddNumber.getText().toString();
                                cv.put("number",s);
                                dbWrite.insert("number", null, cv);
                                refreshListView();
                            }
                        }).setNegativeButton("Cancel",null)
                        .show();

            }
        });

        databaseNumber = new DatabaseNumber(this);
        dbRead = databaseNumber.getReadableDatabase();
        dbWrite =databaseNumber.getWritableDatabase();

        adapter = new SimpleCursorAdapter(this,R.layout.number_list,null, new String[]{"number"}, new int[]{R.id.tvNumber});
        setListAdapter(adapter);
        refreshListView();

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(NumberActivity.this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = adapter.getCursor();
                        c.moveToPosition(position);

                        int itemId = c.getInt(c.getColumnIndex("_id"));
                        dbWrite.delete("number","_id=?", new String[]{itemId+""});
                        refreshListView();
                    }
                }).setNegativeButton("Cancel",null).setTitle("Remind").setMessage("Are you sure you want to delete this?").show();

                return true;
            }
        });

    }

    private void refreshListView() {
        Cursor cursor = dbRead.query("number",null,null,null,null,null,null);
        adapter.changeCursor(cursor);
    }


}
