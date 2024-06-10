package com.example.banruou.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banruou.data.model.UserResponse
import com.example.banruou.data.reponsive.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _user = MutableLiveData<UserResponse?>()
    val user: MutableLiveData<UserResponse?> = _user

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            val result = repository.fetchUserById(userId)
            _user.value = result
        }
    }
}

