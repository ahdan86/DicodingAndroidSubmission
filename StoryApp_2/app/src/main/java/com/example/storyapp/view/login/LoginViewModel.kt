package com.example.storyapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.LoginResponse
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference): ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _loginSuccess = MutableLiveData<Boolean?>()
    val loginSuccess: MutableLiveData<Boolean?> = _loginSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse != null){
                        Log.d(TAG, "Hasil onResponse: Tidak Error")
                        _loginSuccess.value = true
                        val id = loginResponse.loginResult?.userId.toString()
                        val token = loginResponse.loginResult?.token.toString()
                        login(id, token)
                    }
                    else{
                        Log.e(TAG, "ERROR LOGIN USER: ${loginResponse?.message}")
                    }
                }
                else if(response.code() == 401){
                    _loginSuccess.value = false
                }
                _isLoading.value = false
            }
            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
        _loginSuccess.value = null
    }

    fun login(id: String, token: String) {
        viewModelScope.launch {
            pref.login(id,token)
        }
    }
}