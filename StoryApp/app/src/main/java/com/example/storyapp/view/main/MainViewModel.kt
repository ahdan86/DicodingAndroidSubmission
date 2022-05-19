package com.example.storyapp.view.main

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.ListStoryItem
import com.example.storyapp.model.StoriesResponse
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel(private val pref: UserPreference): ViewModel() {
    private val _listPhoto = MutableLiveData<List<ListStoryItem>>()
    val listPhoto: LiveData<List<ListStoryItem>> = _listPhoto

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getListStories(token: String){
        _isLoading.value = true
        val bearerToken = "Bearer $token"
        val client = ApiConfig.getApiService().getStories(bearerToken)
        client.enqueue(object: retrofit2.Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listPhoto.value = response.body()?.listStory as List<ListStoryItem>
                    Log.d("MainViewModel", _listPhoto.value.toString())
                }else{
                    Log.e("MainViewModel", "response onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}