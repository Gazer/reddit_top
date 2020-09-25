package com.example.reddittop.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_entries")
data class TopEntry(
    @PrimaryKey(autoGenerate = true)
    val position: Int,
    val id: String,
    val author: String,
    val title: String,
    val comments: Int,
    val created: Long,
    val url: String,
    val pending: Boolean,
    val imageUrl: String?,
)
