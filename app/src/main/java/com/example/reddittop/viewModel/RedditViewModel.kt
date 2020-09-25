package com.example.reddittop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.reddittop.dao.AppDatabase
import com.example.reddittop.dao.TopEntry
import com.example.reddittop.networking.RedditAPI
import com.example.reddittop.networking.TopEntriesMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RedditViewModel(private val database: AppDatabase) : ViewModel() {
    private val _currentItem = MutableLiveData<TopEntry>()
    val currentItem = _currentItem as LiveData<TopEntry>

    private val _itemRemoved = MutableLiveData<Int>()
    val itemRemoved = _itemRemoved as LiveData<Int>

    init {
        // Cleanup database before start
        viewModelScope.launch(Dispatchers.IO) {
            database.remoteKeysDao().clearRemoteKeys()
            database.topEntriesDao().clearAll()
        }
    }

    fun showItem(item: TopEntry) {
        _currentItem.postValue(item)
        viewModelScope.launch(Dispatchers.IO) {
            database.topEntriesDao().markAsReaded(item.id)
        }
    }

    fun dismissItem(item: TopEntry) {
        if (item == _currentItem.value) {
            _currentItem.postValue(null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            database.topEntriesDao().delete(item)
        }
    }

    fun dismissAll() {
        _currentItem.postValue(null)
        viewModelScope.launch(Dispatchers.IO) {
            database.remoteKeysDao().clearRemoteKeys()
            database.topEntriesDao().clearAll()
        }
    }

    val topEntries = Pager(
        PagingConfig(pageSize = 10),
        remoteMediator = TopEntriesMediator(RedditAPI.invoke(), database)
    ) {
        database.topEntriesDao().pagingSource()
    }.flow.cachedIn(viewModelScope)
}