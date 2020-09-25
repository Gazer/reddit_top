package com.example.reddittop.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val entryId: String,
    val prevKey: String?,
    val nextKey: String?
)