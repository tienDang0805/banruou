package com.example.banruou.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banruou.data.reponsive.UserRepository

import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
     val TAG="RegisterViewModel"
    private val userRepository = UserRepository()

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> get() = _registrationResult

    fun register(userRegister: UserRegister) {
        Log.d(TAG,"User" +userRegister)
        viewModelScope.launch {
            val result = userRepository.registerUser(userRegister)
            _registrationResult.postValue(result)
        }
    }
}