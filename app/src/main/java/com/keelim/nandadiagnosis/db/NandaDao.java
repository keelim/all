package com.keelim.nandadiagnosis.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface NandaDao {

    @Query("SELECT * FROM nanda")
    List<Nanda> getAllNanda();

    @Query("DELETE FROM nanda ")
    void clearAll();

    @Insert
    void insert(Nanda nanda);

    @Update
    void update(Nanda nanda);

    @Delete
    void delete(Nanda nanda);
}
