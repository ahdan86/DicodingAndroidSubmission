package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.api.ApiService
import com.example.storyapp.database.StoriesDatabase
import com.example.storyapp.model.ListStoryItem

class StoriesRepository(private val storiesDatabase: StoriesDatabase, private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoriesRemoteMediator("Bearer $token", storiesDatabase, apiService),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStories()
            }
        ).liveData
    }
}