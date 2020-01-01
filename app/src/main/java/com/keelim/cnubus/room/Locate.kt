package com.keelim.cnubus

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Person(
        @PrimaryKey val id: Int,
        val name: String,
        val job: String
)