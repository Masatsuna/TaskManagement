package org.t_robop.masatsuna.taskmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Masatsuna on 2016/06/04.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    static final String DB = "task.db";
    static final String CREATE_TABLE ="create table tasktable(month int, date int, taskname text, detail text)";
    static final String DROP_TABLE = "drop table tasktable";

    public MySQLiteOpenHelper(Context c) {
        super(c,DB,null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE);
        onCreate(db);

    }
}
