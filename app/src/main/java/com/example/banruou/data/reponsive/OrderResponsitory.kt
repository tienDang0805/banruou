package com.example.banruou.data.reponsive

import PhieuDat
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.network.RetrofitInstance

class OrderResponsitory {

    suspend fun fetchOrderByCustomer(idCustomer: String): List<PhieuDat> {
        return try {
            val response = RetrofitInstance.apiService.getOrderByCustomer(idCustomer)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getOrderDetailByIdOrder(idOrder: String): PhieuDat? {
        return try {
            val response = RetrofitInstance.apiService.getOrderDetailByIdOrder(idOrder)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}