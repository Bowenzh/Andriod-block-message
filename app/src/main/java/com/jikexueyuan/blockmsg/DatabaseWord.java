package com.jikexueyuan.blockmsg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bowenzhang on 15/11/22.
 */
public class DatabaseWord extends SQLiteOpenHelper {

    public DatabaseWord(Context context) {
        super(context, "DatabaseWord",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE word(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "word TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
