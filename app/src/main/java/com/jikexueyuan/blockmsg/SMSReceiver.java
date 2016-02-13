package com.jikexueyuan.blockmsg;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import java.util.Iterator;

/**
 * Created by bowenzhang on 15/11/21.
 */
public class SMSReceiver extends BroadcastReceiver {

    private DatabaseNumber databaseNumber;
    private DatabaseMsg databaseMessage;
    private DatabaseWord databaseWord;
    private SQLiteDatabase dbReadNumber,dbReadWord,dbWrite;
    private String fromAddress;
    private String body;
    private OnMsgBlock onMsgBlock;

    public void setOnMsgBlock(OnMsgBlock onMsgBlock) {
        this.onMsgBlock = onMsgBlock;
    }


    public interface OnMsgBlock{
        public void onMsgBlock();
    }


    @Override
    public void onReceive(Context context, Intent intent) {


        String[] key_word = new String[100];
        String[] key_number = new String[100];
        int in_number = 0;
        int in_word = 0;

        databaseNumber = new DatabaseNumber(context);
        dbReadNumber = databaseNumber.getReadableDatabase();

        Cursor c_number = dbReadNumber.query("number", null, null, null, null, null, null);

        while (c_number.moveToNext()){
            int _id = c_number.getInt(c_number.getColumnIndex("_id"));
            key_number[in_number] = c_number.getString(c_number.getColumnIndex("number"));
            in_number++;
        }

        databaseWord = new DatabaseWord(context);
        dbReadWord = databaseWord.getReadableDatabase();

        Cursor c_word = dbReadWord.query("word", null, null, null, null, null, null);

        while (c_word.moveToNext()){
            int _id = c_word.getInt(c_word.getColumnIndex("_id"));
            key_word[in_word] = c_word.getString(c_word.getColumnIndex("word"));
            in_word++;
        }

        databaseMessage = new DatabaseMsg(context);
        dbWrite = databaseMessage.getWritableDatabase();

        Bundle extras = intent.getExtras();
        if (extras == null)
            return;

        Object[] pdus = (Object[]) extras.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
            fromAddress = message.getOriginatingAddress();
            body = message.getMessageBody();

            for (int i1 = 0; i1 <in_number ; i1++) {
                if (fromAddress.contains(key_number[i1])){
                    abortBroadcast();
                    onMsgBlock.onMsgBlock();
                    ContentValues cv = new ContentValues();
                    cv.put("fromAddress",fromAddress);
                    cv.put("body",body);
                    dbWrite.insert("message",null,cv);
                }
            }

            for (int i2 = 0; i2 < in_word; i2++) {
                if (body.contains(key_word[i2])){
                    abortBroadcast();
                    onMsgBlock.onMsgBlock();
                    ContentValues cv = new ContentValues();
                    cv.put("fromAddress",fromAddress);
                    cv.put("body",body);
                    dbWrite.insert("message", null, cv);
                }

            }

            }


        }



}

