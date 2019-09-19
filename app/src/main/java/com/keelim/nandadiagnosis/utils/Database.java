package com.keelim.nandadiagnosis.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class Database extends SQLiteOpenHelper {  //데이터베이스 사용하기 안드로이드 sqlite
    private static final String DATABASE_NAME = "nanda";
    private String DATABASE_PATH;
    private static final int DATABASE_VERSION = 1;
    private static final String PACKAGE_NAME = "com.keelim.nandadiagnosis";
    private SQLiteDatabase db;

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DATABASE_PATH = getDatabaseFolder() + DATABASE_NAME + ".sqlite";
        db = getWritableDatabase();
    }

    static String getDatabaseFolder() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + PACKAGE_NAME + "/databases/";
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        try {
            if (db != null) {
                if (db.isOpen()) {
                    return db;
                }
            }
            return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
