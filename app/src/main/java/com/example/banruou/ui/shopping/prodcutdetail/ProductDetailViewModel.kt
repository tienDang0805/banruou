package com.example.banruou.ui.shopping.prodcutdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.reponsive.WineRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val repository = WineRepository()
    private val _wineDetail = MutableLiveData<WineLineResponse?>()
    val wineDetail: LiveData<WineLineResponse?> = _wineDetail

    fun fetchWineLinesById(id: String) {
        viewModelScope.launch {
            val result = repository.fetchWineLinesById(id)
            _wineDetail.value = result
            Log.d("WineDetailViewModel", "wine detail: $result")
        }
    }
}