package com.example.reddittop.dao

import androidx.paging.PagingSource
import androidx.room.*


@Dao
interface TopEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<TopEntry>)

    @Query("SELECT * FROM top_entries")
    fun pagingSource(): PagingSource<Int, TopEntry>

    @Delete
    fun delete(entry: TopEntry)

    @Query("DELETE FROM top_entries")
    fun clearAll(): Int
}