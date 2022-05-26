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
//    private val _listPhoto = MutableLiveData<List<ListStoryItem>>()
//    val listPhoto: LiveData<List<ListStoryItem>> = _listPhoto
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading

//    fun getListStories(token: String){
//        _isLoading.value = true
//        val bearerToken = "Bearer $token"
//        val client = ApiConfig.getApiService().getStories(bearerToken, null, null, 0)
//        client.enqueue(object: retrofit2.Callback<StoriesResponse>{
//            override fun onResponse(
//                call: Call<StoriesResponse>,
//                response: Response<StoriesResponse>
//            ) {
//                _isLoading.value = false
//                if(response.isSuccessful){
//                    _listPhoto.value = response.body()?.listStory as List<ListStoryItem>
//                    Log.d("MainViewModel", _listPhoto.value.toString())
//                }else{
//                    Log.e("MainViewModel", "response onFailure: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
//            }
//        })
//    }

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