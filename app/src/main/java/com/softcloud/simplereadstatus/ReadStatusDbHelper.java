package com.softcloud.simplereadstatus;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.annotation.Retention;

/**
 * Created by Softcloud on 16/6/28.
 */
public class ReadStatusDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_read_status";
    private static final int DB_VERSION = 1;

    private static final String CREATE_READ_STATUS_TABLE = "create table ReadStatus ("
            + "id integer primary key autoincrement, "
            + "content_marker text, "
            + "create_time integer, "
            + "delete_time integer) ";

    public ReadStatusDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    public static ReadStatusDbHelper getInstance(Context context){
        return new ReadStatusDbHelper(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_READ_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
