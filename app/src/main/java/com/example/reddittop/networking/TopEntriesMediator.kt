package com.example.reddittop.networking

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.reddittop.dao.AppDatabase
import com.example.reddittop.dao.RemoteKeys
import com.example.reddittop.dao.TopEntry
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TopEntriesMediator @Inject constructor(
    private val api: RedditAPI,
    private val database: AppDatabase
) :
    RemoteMediator<Int, TopEntry>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopEntry>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> null
            // In this example, we never need to prepend, since REFRESH will always load the
            // first page in the list. Immediately return, reporting end of pagination.
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = database.remoteKeysDao().get()
                remoteKeys?.nextKey
            }
        }

        try {
            val apiResponse = api.getTop(page)

            val topEntries = apiResponse.data.children.map {
                TopEntry(
                    0,
                    it.data.id,
                    it.data.author,
                    it.data.title,
                    it.data.num_comments,
                    it.data.created.toLong(),
                    it.data.url,
                    true,
                    it.getThumbnail()
                )
            }
            val endOfPaginationReached = apiResponse.data.after == null
            database.withTransaction {
                // clear all tables in the database
                database.remoteKeysDao().clearRemoteKeys()
                if (loadType == LoadType.REFRESH) {
                    database.topEntriesDao().clearAll()
                }
                val prevKey = apiResponse.data.before
                val nextKey = apiResponse.data.after
                val keys = topEntries.map {
                    RemoteKeys(entryId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.topEntriesDao().insertAll(topEntries)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}