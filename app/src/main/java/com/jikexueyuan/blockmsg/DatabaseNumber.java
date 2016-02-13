package com.jikexueyuan.blockmsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bowenzhang on 15/11/21.
 */
public class DatabaseNumber extends SQLiteOpenHelper {

    public DatabaseNumber(Context context) {
        super(context, "DatabaseNumber", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE number(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "number TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
