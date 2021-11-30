package com.keelim.cnubus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keelim.cnubus.data.db.dao.StationDao
import com.keelim.cnubus.data.db.entity.StationEntity
import com.keelim.cnubus.data.db.entity.StationSubwayCrossRefEntity
import com.keelim.cnubus.data.db.entity.SubwayEntity

@Database(
    entities = [StationEntity::class, SubwayEntity::class, StationSubwayCrossRefEntity::class],
    version = 1,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): StationDao
}