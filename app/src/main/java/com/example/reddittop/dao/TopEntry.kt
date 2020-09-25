package com.example.reddittop.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_entries")
data class TopEntry(
    @PrimaryKey
    val id: String,
    val author: String,
    val title: String,
    val comments: Int,
    val created: Long,
    val imageUrl: String?,
)
