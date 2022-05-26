package com.example.storyapp.injection

import android.content.Context
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.database.StoriesDatabase

object Injection {
    fun provideRepository(context: Context): StoriesRepository{
        val database = StoriesDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(database, apiService)
    }
}