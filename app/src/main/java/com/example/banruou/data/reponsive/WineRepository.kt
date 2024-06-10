package com.example.banruou.data.reponsive

import android.util.Log
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.model.WineTypeResponse
import com.example.banruou.data.network.RetrofitInstance


class WineRepository {
    val TAG ="WineRepository"
    suspend fun fetchWineLines(): List<WineLineResponse> {
        return try {
            val response = RetrofitInstance.apiService.getWineLines()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                // Xử lý lỗi nếu cần
                emptyList()
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần
            emptyList()
        }
    }
    suspend fun fetchWineType(): List<WineTypeResponse> {
        return try {
            val response = RetrofitInstance.apiService.getWineTypes()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                // Xử lý lỗi nếu cần
                emptyList()
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần
            emptyList()
        }
    }
    suspend fun fetchWineLinesByCategory(categoryId: String): List<WineLineResponse> {
        return try {
            val response = RetrofitInstance.apiService.getWineLinesByCategory(categoryId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun fetchWineLinesHot(): List<WineLineResponse> {
        return try {
            val response = RetrofitInstance.apiService.getWineLinesHot()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun fetchWineLinesPromo(): List<WineLineResponse> {
        return try {
            val response = RetrofitInstance.apiService.getWineLinesPromo()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun fetchWineLinesByName(name: String): List<WineLineResponse> {
        return try {
            Log.d(TAG ,"name: " + name)

            val response = RetrofitInstance.apiService.getWineLinesByName(name)
            Log.d(TAG ,"result" + response.body())

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.d(TAG ,"errror: " + e)
            emptyList()
        }
    }
    suspend fun fetchWineLinesById(id: String):WineLineResponse? {
        return try {
            Log.d(TAG ,"name: " + id)

            val response = RetrofitInstance.apiService.getWineLinesById(id)
            Log.d(TAG ,"result" + response.body())

            if (response.isSuccessful) {
                response.body() ?: null
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d(TAG ,"errror: " + e)
           null
        }
    }

}