package com.example.reddittop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reddittop.networking.RedditAPI
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class RedditViewModelFactory @Inject constructor(
    private val api: RedditAPI
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RedditViewModel(api) as T
    }
}