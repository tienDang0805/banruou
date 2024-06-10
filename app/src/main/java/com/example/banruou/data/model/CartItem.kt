package com.example.banruou.data.model

data class CartItem(
    val wineLine: WineLineResponse,
    var quantity: Int = 1
)