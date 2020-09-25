package com.example.reddittop.dao

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 1, entities = [TopEntry::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun topEntriesDao(): TopEntryDao
}
