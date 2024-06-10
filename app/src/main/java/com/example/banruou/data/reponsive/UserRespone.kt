package com.example.banruou.data.reponsive

import com.example.banruou.data.model.UserResponse
import com.example.banruou.data.network.RetrofitInstance
import com.example.banruou.ui.auth.UserRegister

class UserRepository {
    val TAG ="UserRepository"

    suspend fun fetchUserById(userId: String): UserResponse? {
        return try {
            val response = RetrofitInstance.apiService.getUserById(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Xử lý lỗi nếu cần
                null
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần
            null
        }
    }
    suspend fun registerUser(userRegister: UserRegister): Boolean {

        return try {
            val response = RetrofitInstance.apiService.register(userRegister)
            if (response.isSuccessful) {
                true
            }else{
                false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}