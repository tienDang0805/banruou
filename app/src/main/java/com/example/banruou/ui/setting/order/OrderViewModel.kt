package com.example.banruou.ui.setting.order

import PhieuDat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banruou.R
import com.example.banruou.data.model.UserResponse
import com.example.banruou.data.reponsive.OrderResponsitory
import com.example.banruou.data.reponsive.UserRepository
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderViewModel.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderViewModel : ViewModel() {
    private val repository = OrderResponsitory()

    private val _order = MutableLiveData<List<PhieuDat?>>()
    val order: MutableLiveData<List<PhieuDat?>> = _order

    fun fetchOrderByCustomer(userId: String) {
        viewModelScope.launch {
            val result = repository.fetchOrderByCustomer(userId)
            _order.value = result
        }
    }
}