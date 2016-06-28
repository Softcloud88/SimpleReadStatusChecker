package com.softcloud.simplereadstatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Softcloud on 16/6/28.
 */
public class ReadStatusHelper<T> {

    private Context context;
    private ReadableManager<T> readableManager;
    private ReadStatusDbHelper dbHelper;
    private SQLiteDatabase db;

    private static final int DEFAULT_STORE_DAYS = 7;
    private static final long DAY = 1000 * 60 * 60 * 24;

    private static final String READ_STATUS_TABLE_NAME = "ReadStatus";
    private static final String KEY_CONTENT_MARKER = "content_marker";
    private static final String KEY_CREATE_TIME = "create_time";
    private static final String KEY_DELETE_TIME = "delete_time";

    public static ReadStatusHelper create(Context context, ReadableManager readableManager) {
        ReadStatusHelper instance = new ReadStatusHelper();
        instance.context = context;
        instance.readableManager = readableManager;
        instance.initDb();
        return instance;
    }

    public ReadStatusHelper addReadable(String marker) {
        return addReadable(marker, DEFAULT_STORE_DAYS);
    }

    public ReadStatusHelper addReadable(String marker, int daysToStore) {
        if (shouldToClean()) {
            cleanOldData();
        }
        daysToStore = daysToStore < 1 ? DEFAULT_STORE_DAYS : daysToStore;
        getDb().beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CONTENT_MARKER, marker);
            values.put(KEY_CREATE_TIME, System.currentTimeMillis());
            values.put(KEY_DELETE_TIME, System.currentTimeMillis() + daysToStore * DAY);
            getDb().insert(READ_STATUS_TABLE_NAME, null, values);
            getDb().setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getDb().endTransaction();
        }
        return this;
    }

    public ReadStatusHelper checkReadStatus(final List<T> readables) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (T readable : readables) {
                    if (isReadableRead(readable)) {
                        readableManager.markRead(readable);
                    } else {
                        readableManager.markNotRead(readable);
                    }
                }
                readableManager.onCheckFinish(readables);
            }
        }).start();
        return this;
    }

    private boolean isReadableRead(T readable) {
        // TODO: 16/6/29
        return true;
    }

    public ReadStatusHelper cleanOldData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDb().beginTransaction();
                try {
                    getDb().delete(READ_STATUS_TABLE_NAME, "delete_time < ?", new String[]{String.valueOf(System.currentTimeMillis())});
                    getDb().setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getDb().endTransaction();
                }
            }
        }).start();
        return this;
    }

    private SQLiteDatabase getDb() {
        initDb();
        return db;
    }

    private ReadStatusDbHelper getDbHelper() {
        initDbHelper();
        return dbHelper;
    }

    private void initDb() {
        initDbHelper();
        if (db == null) {
            db = dbHelper.getWritableDatabase();
        }
    }

    private void initDbHelper() {
        if (dbHelper == null) {
            dbHelper = ReadStatusDbHelper.getInstance(context);
        }
    }

    private boolean shouldToClean() {
        // TODO: 16/6/28
        return true;
    }
}
