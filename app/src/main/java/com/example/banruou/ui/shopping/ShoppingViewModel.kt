package com.example.banruou.ui.shopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.model.WineTypeResponse
import com.example.banruou.data.reponsive.WineRepository
import kotlinx.coroutines.launch

class ShoppingViewModel : ViewModel() {
    private val repository = WineRepository()

    private val _wineLines = MutableLiveData<List<WineLineResponse>>()
    val wineLines: LiveData<List<WineLineResponse>> = _wineLines

    private val _wineTypes = MutableLiveData<List<WineTypeResponse>>()
    val wineTypes: LiveData<List<WineTypeResponse>> = _wineTypes

    private val _searchResult = MutableLiveData<List<WineLineResponse>>()
    val searchResult: LiveData<List<WineLineResponse>> = _searchResult

    init {
        fetchWineTypes()
    }

    fun fetchWineLines() {
        viewModelScope.launch {
            try {
                val result = repository.fetchWineLines()
                _wineLines.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchWineTypes() {
        viewModelScope.launch {
            try {
                val result = repository.fetchWineType()
                _wineTypes.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchWineLinesByCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val result = repository.fetchWineLinesByCategory(categoryId)
                _wineLines.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchWineLinesByName(name: String) {
        viewModelScope.launch {
            try {
                val result = repository.fetchWineLinesByName(name)
                _searchResult.value = result
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
