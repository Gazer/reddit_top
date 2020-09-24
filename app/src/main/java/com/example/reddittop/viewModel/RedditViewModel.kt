package com.example.reddittop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.reddittop.adapters.TopDataSource
import com.example.reddittop.models.Children
import com.example.reddittop.networking.RedditAPI

class RedditViewModel(private val api: RedditAPI) : ViewModel() {
    private val _currentItem = MutableLiveData<Children>()
    val currentItem = _currentItem as LiveData<Children>

    fun showItem(item: Children) {
        _currentItem.postValue(item)
    }

    val topEntries = Pager(PagingConfig(pageSize = 25)) {
        TopDataSource(api)
    }.flow.cachedIn(viewModelScope)
}