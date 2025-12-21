package com.keelim.model

data class LocalTask(
    val id: String,
    val title: String,
    val description: String,
    var isCompleted: Boolean,
    val date: String,
    val isEditing: Boolean,
)
