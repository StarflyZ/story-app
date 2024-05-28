package com.dicoding.picodiploma.loginwithanimation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        val result = MutableLiveData<Result<LoginResponse>>()
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Logging in with email: $email")
                val response = repository.login(email, password)
                result.postValue(Result.success(response))
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error logging in", e)
                result.postValue(Result.failure(e))
            }
        }
        return result
    }
    fun saveSession(user: UserModel){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
