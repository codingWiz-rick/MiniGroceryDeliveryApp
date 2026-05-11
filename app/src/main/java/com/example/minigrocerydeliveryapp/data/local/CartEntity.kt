package com.example.minigrocerydeliveryapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.minigrocerydeliveryapp.model.CartItem
import com.example.minigrocerydeliveryapp.model.Product

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val quantity: Int
) {
    fun toCartItem(): CartItem {
        return CartItem(
            product = Product(id, name, description, price, imageUrl, category),
            quantity = quantity
        )
    }

    companion object {
        fun fromCartItem(cartItem: CartItem): CartEntity {
            return CartEntity(
                id = cartItem.product.id,
                name = cartItem.product.name,
                description = cartItem.product.description,
                price = cartItem.product.price,
                imageUrl = cartItem.product.imageUrl,
                category = cartItem.product.category,
                quantity = cartItem.quantity
            )
        }
    }
}
