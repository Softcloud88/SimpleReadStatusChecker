package com.softcloud.simplereadstatus.newsStatusUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Created by Softcloud on 16/6/28.
 */
public class ReadStatusHelper<T> {

    private Context context;
    private ReadableManager<T> readableManager;
    private ReadStatusDbHelper dbHelper;
    private SQLiteDatabase db;

    private static final String PREF_KEY_NEWS_STATUS_LAST_CLEAN_TIME = "pref_key_news_status_last_clean_time";
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

    public ReadStatusHelper addReadable(T readable) {
        return addReadable(readable, DEFAULT_STORE_DAYS);
    }

    public ReadStatusHelper addReadable(T readable, int daysToStore) {
        if (shouldToClean() || readableManager == null) {
            cleanOldData();
        }
        String marker = readableManager.getContentMarker(readable);
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
        if (readableManager == null) {
            return this;
        }
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
        boolean hasRead = false;
        try {
            Cursor cursor = getDb().query(READ_STATUS_TABLE_NAME, null, KEY_CONTENT_MARKER + " = ?"
                    , new String[]{readableManager.getContentMarker(readable)}, null, null, null);
            hasRead = cursor.moveToNext();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasRead;
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
        setLongPreference(PREF_KEY_NEWS_STATUS_LAST_CLEAN_TIME, System.currentTimeMillis());
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

    public boolean setLongPreference(String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        } else {
            return false;
        }
    }
}
