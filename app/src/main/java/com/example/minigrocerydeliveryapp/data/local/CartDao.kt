package com.example.minigrocerydeliveryapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(cartEntity: CartEntity)

    @Delete
    suspend fun delete(cartEntity: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
