package com.example.reddittop.di

import com.example.reddittop.networking.RedditAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object  TopModule {
    @Provides
    fun provideRedditApi(): RedditAPI = RedditAPI()
}