package com.example.minigrocerydeliveryapp.model

data class Order(
    val id: String,
    val date: String,
    val totalAmount: Double,
    val status: String,
    val items: List<CartItem>
)