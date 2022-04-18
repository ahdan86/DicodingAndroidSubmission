package com.ahdan.githubuser2.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.ahdan.githubuser2.api.ApiConfig
import com.ahdan.githubuser2.model.ItemsItem
import com.ahdan.githubuser2.model.SearchResponse
import com.ahdan.githubuser2.preferences.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _listUser = MutableLiveData<ArrayList<ItemsItem>>()
    val listUser: LiveData<ArrayList<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    private fun findUser(user: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(user)
        client.enqueue(object: Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _listUser.value = response.body()?.items as ArrayList<ItemsItem>
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun setFindUser(user: String){
        findUser(user)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}