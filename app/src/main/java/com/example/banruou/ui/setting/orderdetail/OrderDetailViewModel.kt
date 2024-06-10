package com.example.banruou.ui.setting.orderdetail

import PhieuDat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.banruou.data.reponsive.OrderResponsitory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OrderDetailViewModel:ViewModel() {
    private val repository = OrderResponsitory()
    val orderDetail = MutableLiveData<PhieuDat?>()

    fun fetchOrderDetail(orderId: String) {
        viewModelScope.launch {
            Log.d("OrderDetails","orderId $orderId")
            val result = repository.getOrderDetailByIdOrder(orderId)
            orderDetail.value = result
            Log.d("OrderDetails", "OrderDetails" + result)
        }
    }
}