package com.keelim.nandadiagnosis.mainFragment.search.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //todo CUrsor 가 뭐지?
    //todo -> 나중에 jetpack Room 사용을 고려

    private static final String DATABSAE_NAME = "nanda.db";
    private static final String TABLE_NAME = "nanda";
    private static final String COL_1 = "nanda_id";
    private static final String COL_2 = "reason";
    private static final String COL_3 = "diagnosis";
    private static final String COL_4 = "class_name";
    private static final String COL_5 = "domain_name";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABSAE_NAME, null, 1);
    }

    public List<DatabaseItem> search(String keyword) {
        List<DatabaseItem> dbItems = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select DISTINCT * from " + TABLE_NAME + " where " + COL_2 + " like ?", new String[]{"%" + keyword + "%"});
            if (cursor.moveToFirst()) {
                dbItems = new ArrayList<>();
                do {
                    DatabaseItem databaseItem = new DatabaseItem();
                    databaseItem.setPrimaryKey(cursor.getInt(0));
                    databaseItem.setReason(cursor.getString(1));
                    databaseItem.setDiagnosis(cursor.getString(2));
                    databaseItem.setClass_name(cursor.getString(3));
                    databaseItem.setDomain_name(cursor.getString(4));
                    dbItems.add(databaseItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            dbItems = null;
        }
        return dbItems;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { //database 만드러질 활용을 하는 것

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { //Upgrade 할때만 사용

    }
}
