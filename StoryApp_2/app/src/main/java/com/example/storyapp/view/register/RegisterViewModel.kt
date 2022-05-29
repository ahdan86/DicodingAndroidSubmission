package com.example.storyapp.view.register

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.RegisterResponse
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel() : ViewModel() {
    companion object {
        private const val TAG = "RegisterViewModel"
    }

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    fun postUser(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().getUsers(name, email, password)
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        if(data.error == false){
                            Log.d(TAG, "Success onResponse: ${data.message}")
                            _registerSuccess.value = true
                        }
                        else{
                            Log.e(TAG, "ERROR CREATING USER: ${data.message}")
                            _registerSuccess.value = false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _registerSuccess.value = false
            }
        })
    }
}