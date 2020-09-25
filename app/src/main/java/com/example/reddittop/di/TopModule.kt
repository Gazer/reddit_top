package com.example.reddittop.di

import android.content.Context
import androidx.room.Room
import com.example.reddittop.dao.AppDatabase
import com.example.reddittop.networking.RedditAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TopModule {
    @Provides
    fun provideRedditApi(): RedditAPI = RedditAPI()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "top.db"
    ).fallbackToDestructiveMigration().build()
}