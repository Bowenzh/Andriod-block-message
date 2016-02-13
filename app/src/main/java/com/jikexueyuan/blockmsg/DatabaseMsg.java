package com.jikexueyuan.blockmsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bowenzhang on 15/11/21.
 */
public class DatabaseMsg extends SQLiteOpenHelper {


    public DatabaseMsg(Context context) {
        super(context, "DatabaseMsg", null, 1);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE message(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "body TEXT DEFAULT \"\"," +
                "date TEXT DEFAULT \"\"," +
                "fromAddress TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
