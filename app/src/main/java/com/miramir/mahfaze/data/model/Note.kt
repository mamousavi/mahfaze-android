package com.miramir.mahfaze.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String,
    var text: String,
    val createdAt: String,
    var updatedAt: String,
    var deletedAt: String?,
    val userId: Int
)