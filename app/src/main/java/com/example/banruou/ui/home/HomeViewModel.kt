package com.example.banruou.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.model.WineTypeResponse
import com.example.banruou.data.reponsive.WineRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = WineRepository()

    private val _wineLines = MutableLiveData<List<WineLineResponse>>()
    val wineLines: LiveData<List<WineLineResponse>> = _wineLines
    private val _wineTypes = MutableLiveData<List<WineTypeResponse>>()
    val wineTypes: LiveData<List<WineTypeResponse>> = _wineTypes
    private val _searchResult = MutableLiveData<List<WineLineResponse>>()
    val searchResult: LiveData<List<WineLineResponse>> = _searchResult
    private var loadedItems = 0
    private var totalItems = 0
    init {
        //fetchWineLines()
        //fetchWineType()
        fetchWineLinesHot()
    }

    private fun fetchWineLines() {
        viewModelScope.launch {
            val result = repository.fetchWineLines()
            _wineLines.value = result
        }
    }
    private fun fetchWineType() {
        viewModelScope.launch {
            val result = repository.fetchWineType()
            _wineTypes.value = result
        }
    }
    fun fetchWineLinesByCategory(categoryId: String) {
        viewModelScope.launch {
            val result = repository.fetchWineLinesByCategory(categoryId)
            _wineLines.value = result
        }
    }
    fun fetchWineLinesHot() {
        viewModelScope.launch {
            val result = repository.fetchWineLinesHot()
            _wineLines.value = result
        }
    }
    fun fetchWineLinesPromo() {
        viewModelScope.launch {
            val result = repository.fetchWineLinesPromo()
            _wineLines.value = result
        }
    }
    fun fetchWineLinesByName(name: String) {
        viewModelScope.launch {
            val result = repository.fetchWineLinesByName(name)
            _searchResult.value = result
            Log.d("HomeViewModel", "search result: $result")
        }
    }


}