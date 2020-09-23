package com.example.reddittop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.reddittop.adapters.TopDataSource
import com.example.reddittop.networking.RedditAPI

class RedditViewModel(private val api: RedditAPI) : ViewModel() {
    val topEntries = Pager(PagingConfig(pageSize = 25)) {
        TopDataSource(api)
    }.flow.cachedIn(viewModelScope)
}