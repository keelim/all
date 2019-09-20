package com.keelim.nandadiagnosis.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities ={Nanda.class}, version = 1)
public abstract class NandaRoomDatabase extends RoomDatabase{
    public abstract NandaDao nandaDao();

    private static volatile NandaRoomDatabase INSTANCE;

    static NandaRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (NandaRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NandaRoomDatabase.class,"nanda.db").build();
                }
            }
        }
        return INSTANCE;
    }
}