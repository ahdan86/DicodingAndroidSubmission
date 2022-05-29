package com.example.storyapp.view.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.model.ListStoryItem
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference, private val storiesRepository: StoriesRepository): ViewModel() {
    fun stories(token: String) : LiveData<PagingData<ListStoryItem>> = storiesRepository.getStories(token).cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}