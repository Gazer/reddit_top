package com.example.reddittop.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reddittop.models.RedditListing
import com.example.reddittop.networking.RedditAPI
import kotlinx.coroutines.launch

class RedditViewModel(private val api:RedditAPI): ViewModel() {
    val topEntries = MutableLiveData<RedditListing>()

    fun loadTop() {
        viewModelScope.launch {
            topEntries.postValue(api.getTop())
        }
    }
}