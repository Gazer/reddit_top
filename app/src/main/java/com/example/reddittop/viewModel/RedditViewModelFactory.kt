package com.example.reddittop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reddittop.dao.AppDatabase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RedditViewModelFactory @Inject constructor(
    private val database: AppDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RedditViewModel(database) as T
    }
}