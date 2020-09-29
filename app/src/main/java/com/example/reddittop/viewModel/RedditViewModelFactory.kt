package com.example.reddittop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reddittop.dao.AppDatabase
import com.example.reddittop.networking.TopEntriesMediator
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RedditViewModelFactory @Inject constructor(
    private val database: AppDatabase,
    private val topEntriesMediator: TopEntriesMediator,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RedditViewModel(database, topEntriesMediator) as T
    }
}