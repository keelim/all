package com.keelim.cnubus

import androidx.room.*

@Dao
interface PersonDao {
    @Query("select * from person")
    fun getAllPerson(): List<Person>

    @Query("delete from person")
    fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg person:Person)

    @Update
    fun update(vararg person:Person)

    @Delete
    fun delete(vararg person:Person)

}