package com.example.minigrocerydeliveryapp.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String, // Web image URL
    val category: String
)

data class CartItem(
    val product: Product,
    var quantity: Int
)
