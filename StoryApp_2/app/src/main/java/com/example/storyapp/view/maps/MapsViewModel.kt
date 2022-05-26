package com.example.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.ListStoryItem
import com.example.storyapp.model.StoriesResponse

class MapsViewModel(): ViewModel() {
    var _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    fun getListStories(token: String){
        val bearerToken = "Bearer $token"
        val client = ApiConfig.getApiService().getMapStories(bearerToken, 1)
        client.enqueue(object: retrofit2.Callback<StoriesResponse>{
            override fun onResponse(
                call: retrofit2.Call<StoriesResponse>,
                response: retrofit2.Response<StoriesResponse>
            ) {
                if (response.isSuccessful){
                    _listStories.value = response.body()?.listStory as List<ListStoryItem>
                    Log.d("Success", _listStories.value.toString())
                }else{
                    Log.e("MapsViewModel", "response onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<StoriesResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}