package com.jikexueyuan.blockmsg;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private SimpleCursorAdapter adapter;
    private Button btnMainNumber,btnMainWord;
    private DatabaseMsg databaseMessage;
    private SQLiteDatabase dbRead,dbWrite;
    private SMSReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMainNumber = (Button) findViewById(R.id.btnMainNumber);
        btnMainWord = (Button) findViewById(R.id.btnMainWord);

        btnMainNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NumberActivity.class);
                startActivity(i);
            }
        });

        btnMainWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WordActivity.class);
                startActivity(i);
            }
        });

        databaseMessage = new DatabaseMsg(this);
        dbWrite =databaseMessage.getWritableDatabase();
        dbRead = databaseMessage.getReadableDatabase();

        adapter = new SimpleCursorAdapter(this,R.layout.msg_list,null, new String[]{"fromAddress","body"}, new int[]{R.id.tvShowAddress,R.id.tvShowBody});
        setListAdapter(adapter);
        refreshListView();

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = adapter.getCursor();
                        c.moveToPosition(position);

                        int itemId = c.getInt(c.getColumnIndex("_id"));
                        dbWrite.delete("message", "_id=?", new String[]{itemId + ""});
                        refreshListView();
                    }
                }).setNegativeButton("Cancel", null).setTitle("Remind").setMessage("Are you sure you want to delete this?").show();

                return true;
            }
        });

        SMSReceiver smsReceiver = new SMSReceiver();
        registerReceiver(smsReceiver, new IntentFilter());
        smsReceiver.setOnMsgBlock(new SMSReceiver.OnMsgBlock() {
            @Override
            public void onMsgBlock() {
                refreshListView();
            }
        });

    }

    public void refreshListView() {
        Cursor cursor = dbRead.query("message",null,null,null,null,null,null);
        adapter.changeCursor(cursor);
    }

}
