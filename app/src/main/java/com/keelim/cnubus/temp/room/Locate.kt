package com.keelim.cnubus

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Locate(
        @PrimaryKey val id: Int,
        val xlocation: Double,
        val ylocation: Double
)