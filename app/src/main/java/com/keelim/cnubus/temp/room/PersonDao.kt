package com.keelim.cnubus

import androidx.room.*

@Dao
interface PersonDao {
    @Query("select * from person")
    fun getAllPerson(): List<Locate>

    @Query("delete from person")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg locates:Locate)

    @Update
    fun update(vararg locates:Locate)

    @Delete
    fun delete(vararg locates:Locate)

}