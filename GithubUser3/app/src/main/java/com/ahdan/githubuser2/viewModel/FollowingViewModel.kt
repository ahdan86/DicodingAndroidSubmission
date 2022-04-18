package com.ahdan.githubuser2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahdan.githubuser2.api.ApiConfig
import com.ahdan.githubuser2.model.FollowingResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {
    private val _listFollowing = MutableLiveData<ArrayList<FollowingResponseItem>>()
    val listFollowing: LiveData<ArrayList<FollowingResponseItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowingViewModel"
    }

    private fun listFollowing(user: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListFollowing(user)
        client.enqueue(object: Callback<List<FollowingResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowingResponseItem>>,
                response: Response<List<FollowingResponseItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listFollowing.value = response.body() as ArrayList<FollowingResponseItem>
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun setListFollowing(user: String){
        listFollowing(user)
    }
}