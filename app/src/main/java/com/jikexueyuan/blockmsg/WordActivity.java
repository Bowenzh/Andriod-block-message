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

public class WordActivity extends ListActivity {

    private SimpleCursorAdapter adapter;
    private Button btnAddWord;
    private DatabaseWord databaseWord;
    private SQLiteDatabase dbRead,dbWrite;
    private EditText etAddWord;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        btnAddWord = (Button) findViewById(R.id.btnAddWord);

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(WordActivity.this, R.layout.word_input,null);
                etAddWord = (EditText) view.findViewById(R.id.etAddWord);
                new AlertDialog.Builder(WordActivity.this)
                        .setTitle("Please to enter a number")
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues cv = new ContentValues();
                                String s = etAddWord.getText().toString();
                                cv.put("word",s);
                                dbWrite.insert("word", null, cv);
                                refreshListView();
                            }
                        }).setNegativeButton("Cancel",null)
                        .show();

            }
        });

        databaseWord = new DatabaseWord(this);
        dbRead = databaseWord.getReadableDatabase();
        dbWrite =databaseWord.getWritableDatabase();

        adapter = new SimpleCursorAdapter(this,R.layout.word_list,null, new String[]{"word"}, new int[]{R.id.tvWord});
        setListAdapter(adapter);
        refreshListView();

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(WordActivity.this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = adapter.getCursor();
                        c.moveToPosition(position);

                        int itemId = c.getInt(c.getColumnIndex("_id"));
                        dbWrite.delete("word","_id=?", new String[]{itemId+""});
                        refreshListView();
                    }
                }).setNegativeButton("Cancel",null).setTitle("Remind").setMessage("Are you sure you want to delete this?").show();
                return true;
            }
        });

    }

    private void refreshListView() {
        Cursor cursor = dbRead.query("word",null,null,null,null,null,null);
        adapter.changeCursor(cursor);
    }

}