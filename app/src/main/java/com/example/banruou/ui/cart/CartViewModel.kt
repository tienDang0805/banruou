package com.example.banruou.ui.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.banruou.data.model.CartItem
import com.example.banruou.data.model.WineLineResponse

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> get() = _cartItems

    fun addItemToCart(item: WineLineResponse) {
        Log.d("CartViewModel", "addItemToCart: $item")
        val existingItem = _cartItems.value?.find { it.wineLine.MADONG == item.MADONG }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.value?.add(CartItem(item))
        }
        _cartItems.value = _cartItems.value // Trigger LiveData update
    }

    fun updateCartItem(cartItem: CartItem) {
        val index = _cartItems.value?.indexOfFirst { it.wineLine.MADONG == cartItem.wineLine.MADONG }
        if (index != null && index != -1) {
            _cartItems.value?.set(index, cartItem)
            _cartItems.value = _cartItems.value // Trigger LiveData update
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        _cartItems.value?.remove(cartItem)
        _cartItems.value = _cartItems.value // Trigger LiveData update
    }

    fun updateCartItems(newCartItems: List<CartItem>) {
        _cartItems.value = newCartItems.toMutableList()
    }
}
