package com.example.reddittop

import android.app.Application
import com.example.reddittop.dao.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class TopApplication : Application() {
    @Inject
    lateinit var database: AppDatabase

}