package com.example.reddittop.dao

import androidx.paging.PagingSource
import androidx.room.*


@Dao
interface TopEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<TopEntry>)

    @Query("SELECT * FROM top_entries ORDER BY position ASC")
    fun pagingSource(): PagingSource<Int, TopEntry>

    @Delete
    suspend fun delete(entry: TopEntry)

    @Query("UPDATE top_entries SET pending=0 WHERE id=:id")
    suspend fun markAsReaded(id: String): Int

    @Query("DELETE FROM top_entries")
    suspend fun clearAll(): Int
}