package com.keelim.cnubus.data.db.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.keelim.cnubus.data.db.AppDatabase
import com.keelim.cnubus.data.db.entity.Comment
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentDaoTest {
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            appContext,
            AppDatabase::class.java
        ).build()
    }

    @Test
    fun addFavorite() = runBlocking {
        val item = Comment(0, "keelim", "keelim")
        db.daoComment().addFavorite(item)

        var schema = db.daoComment().getAll()[0]
        assertEquals(schema.owner, item.owner)
        assertEquals(schema.description, item.description)

        db.daoComment().removeFavorite(item)
        assertEquals(0, db.daoComment().getAll().size)
    }

    @After
    fun cleanup() = runBlocking {

    }
}