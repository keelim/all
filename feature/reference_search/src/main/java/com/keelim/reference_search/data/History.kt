package com.keelim.reference_search.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey val uid:Int?,
    @ColumnInfo(name="keyword") val keyword:String?
)
