package com.example.reddittop.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TopEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<TopEntry>)

    @Query("SELECT * FROM top_entries")
    fun pagingSource(): PagingSource<Int, TopEntry>

    @Query("DELETE FROM top_entries")
    fun clearAll(): Int
}