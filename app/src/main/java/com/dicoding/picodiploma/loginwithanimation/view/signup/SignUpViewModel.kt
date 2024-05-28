package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        val result = MutableLiveData<Result<RegisterResponse>>()
        viewModelScope.launch {
            try {
                Log.d("SignUpViewModel", "Registering user with name: $name, email: $email")
                val response = repository.register(name, email, password)
                result.postValue(Result.success(response))
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Error registering user", e)
                result.postValue(Result.failure(e))
            }
        }
        return result
    }
}