package com.keelim.nandadiagnosis.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABSAE_NAME = "nanda.db";
    public static final String TABLE_NAME = "nandaraw";
    public static final String COL_1 = "id";
    public static final String COL_2 = "reason";
    public static final String COL_3 = "diagnosis";
    public static final String COL_4 = "class_name";
    public static final String COL_5 = "domain_name";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABSAE_NAME, null, 1);
    }

    public List<DBitem> search(String keyword) {
        List<DBitem> dBitems = null;
        try {
            dBitems = null;
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + COL_2 + " like ?", new String[]{"%" + keyword + "%"});
            if (cursor.moveToFirst()) {
                dBitems = new ArrayList<>();
                do {
                    DBitem dBitem = new DBitem();
                    dBitem.setId(cursor.getInt(0));
                    dBitem.setReason(cursor.getString(1));
                    dBitem.setDiagnosis(cursor.getString(2));
                    dBitem.setClass_name(cursor.getString(3));
                    dBitem.setDomain_name(cursor.getString(4));
                    dBitems.add(dBitem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            dBitems = null;
        }
        return dBitems;
    }

    public List<DBitem> findAll() {
        List<DBitem> dBitems = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                dBitems = new ArrayList<>();
                do {
                    DBitem dBitem = new DBitem();
                    dBitem.setId(cursor.getInt(0));
                    dBitem.setReason(cursor.getString(1));
                    dBitem.setDiagnosis(cursor.getString(2));
                    dBitem.setClass_name(cursor.getString(3));
                    dBitem.setDomain_name(cursor.getString(4));
                    dBitems.add(dBitem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            dBitems = null;
        }
        return dBitems;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
