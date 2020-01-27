package com.keelim.cnubus.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey
    val location_id: Int,

    val location1: Long,

    val location2: Long
)
