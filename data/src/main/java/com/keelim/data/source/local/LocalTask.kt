package com.keelim.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keelim.data.source.Task
import com.keelim.data.source.network.NetworkTask

@Entity(
    tableName = "task"
)
data class LocalTask(
    @PrimaryKey val id: String,
    var title: String,
    var description: String,
    var isCompleted: Boolean,
)
