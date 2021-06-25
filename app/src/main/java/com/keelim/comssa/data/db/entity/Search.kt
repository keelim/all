package com.keelim.comssa.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Search")
data class Search(
    @PrimaryKey
    val id: Int,
    val category: String?,
    val title: String?,
    val description: String?,
    val favorite: Int,
    var relation: String?,
)
