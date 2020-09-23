package com.example.reddittop.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reddittop.networking.RedditAPI
import kotlinx.coroutines.launch

class RedditViewModel(private val api:RedditAPI): ViewModel() {
    fun loadTop() {
        viewModelScope.launch {
            val top = api.getTop()
            Log.d("Listing", top.toString());
        }
    }
}