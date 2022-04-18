package com.ahdan.githubuser2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahdan.githubuser2.api.ApiConfig
import com.ahdan.githubuser2.model.FollowerResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel: ViewModel() {
    private val _listFollower = MutableLiveData<ArrayList<FollowerResponseItem>>()
    val listFollower: LiveData<ArrayList<FollowerResponseItem>> = _listFollower

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowerViewModel"
    }

    private fun listFollower(user: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListFollower(user)
        client.enqueue(object: Callback<List<FollowerResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowerResponseItem>>,
                response: Response<List<FollowerResponseItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listFollower.value = response.body()as ArrayList<FollowerResponseItem>
                }else {
                    Log.e(TAG, "successOnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowerResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "failureOnFailure: ${t.message.toString()}")
            }
        })
    }

    fun setListFollower(user: String){
        listFollower(user)
    }
}