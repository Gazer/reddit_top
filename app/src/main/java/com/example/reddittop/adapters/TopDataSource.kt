package com.example.reddittop.adapters

import androidx.paging.PagingSource
import com.example.reddittop.models.Children
import com.example.reddittop.networking.RedditAPI

class TopDataSource(private val api:RedditAPI) : PagingSource<String, Children>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Children> {
        return try {
            val after = params.key ?: null
            val response = api.getTop(after)
            LoadResult.Page(
                data = response.data.children,
                prevKey = response.data.before,
                nextKey = response.data.after
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}