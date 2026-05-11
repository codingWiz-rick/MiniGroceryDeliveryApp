package com.example.minigrocerydeliveryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minigrocerydeliveryapp.data.PreferenceManager
import com.example.minigrocerydeliveryapp.data.ProductRepository
import com.example.minigrocerydeliveryapp.data.local.AppDatabase
import com.example.minigrocerydeliveryapp.data.local.CartEntity
import com.example.minigrocerydeliveryapp.model.CartItem
import com.example.minigrocerydeliveryapp.model.Order
import com.example.minigrocerydeliveryapp.model.Product
import com.example.minigrocerydeliveryapp.model.UserAddress
import com.example.minigrocerydeliveryapp.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroceryViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceManager = PreferenceManager(application)
    private val cartDao = AppDatabase.getDatabase(application).cartDao()
    
    // List of all products from repository
    private val allProducts = ProductRepository.dummyProducts

    // Categories available
    val categories = listOf("All") + allProducts.map { it.category }.distinct()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Filtered products list
    private val _products = MutableStateFlow(allProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Cart state from Room DB
    val cartItems: StateFlow<List<CartItem>> = cartDao.getAllCartItems()
        .map { entities -> entities.map { it.toCartItem() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Total price calculation
    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    // User Profile state
    private val _userProfile = MutableStateFlow(preferenceManager.getUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // User Address state
    private val _userAddress = MutableStateFlow(preferenceManager.getAddress())
    val userAddress: StateFlow<UserAddress> = _userAddress.asStateFlow()

    // Address list state
    private val _addressList = MutableStateFlow<List<UserAddress>>(preferenceManager.getAddressList())
    val addressList: StateFlow<List<UserAddress>> = _addressList.asStateFlow()

    // Order history state
    private val _orders = MutableStateFlow<List<Order>>(preferenceManager.getOrders())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    /**
     * Updates the user's profile information
     */
    fun updateProfile(newProfile: UserProfile) {
        _userProfile.value = newProfile
        preferenceManager.saveUserProfile(newProfile)
    }

    /**
     * Updates the user's delivery address and saves it as current
     */
    fun updateAddress(newAddress: UserAddress) {
        _userAddress.value = newAddress
        preferenceManager.saveAddress(newAddress)
        
        // Also add to address list if it's a complete new address
        if (!_addressList.value.contains(newAddress) && newAddress.isComplete()) {
            addAddress(newAddress)
        }
    }

    /**
     * Adds an address to the saved addresses list
     */
    fun addAddress(address: UserAddress) {
        if (_addressList.value.contains(address)) return
        
        val updatedList = _addressList.value + address
        _addressList.value = updatedList
        preferenceManager.saveAddressList(updatedList)
    }

    /**
     * Removes an address from the saved addresses list
     */
    fun removeAddress(address: UserAddress) {
        val updatedList = _addressList.value.filter { it != address }
        _addressList.value = updatedList
        preferenceManager.saveAddressList(updatedList)
        
        // If the removed address was the current one, clear the current address
        if (_userAddress.value == address) {
            val emptyAddress = UserAddress()
            _userAddress.value = emptyAddress
            preferenceManager.saveAddress(emptyAddress)
        }
    }

    /**
     * Places an order and adds it to history
     */
    fun placeOrder() {
        val currentCart = cartItems.value
        if (currentCart.isNotEmpty()) {
            val newOrder = Order(
                id = "ORD-${System.currentTimeMillis() % 100000}",
                date = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date()),
                totalAmount = totalPrice.value,
                status = "Delivered",
                items = currentCart
            )
            val updatedOrders = listOf(newOrder) + _orders.value
            _orders.value = updatedOrders
            preferenceManager.saveOrders(updatedOrders)
            clearCart()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    private fun applyFilters() {
        val category = _selectedCategory.value
        val query = _searchQuery.value.lowercase()

        _products.value = allProducts.filter { product ->
            val matchesCategory = (category == "All" || product.category == category)
            val matchesSearch = product.name.lowercase().contains(query)
            matchesCategory && matchesSearch
        }
    }

    /**
     * Adds a product to the cart using Room. 
     */
    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val existingItem = cartItems.value.find { it.product.id == product.id }
            if (existingItem != null) {
                cartDao.insertOrUpdate(CartEntity.fromCartItem(existingItem.copy(quantity = existingItem.quantity + quantity)))
            } else {
                cartDao.insertOrUpdate(CartEntity.fromCartItem(CartItem(product, quantity)))
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.delete(CartEntity.fromCartItem(cartItem))
        }
    }

    fun incrementQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.insertOrUpdate(CartEntity.fromCartItem(cartItem.copy(quantity = cartItem.quantity + 1)))
        }
    }

    fun decrementQuantity(cartItem: CartItem) {
        viewModelScope.launch {
            if (cartItem.quantity > 1) {
                cartDao.insertOrUpdate(CartEntity.fromCartItem(cartItem.copy(quantity = cartItem.quantity - 1)))
            } else {
                cartDao.delete(CartEntity.fromCartItem(cartItem))
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    }

    /**
     * Clears the user session and orders
     */
    fun logout() {
        preferenceManager.clearSession()
        _orders.value = emptyList()
        _addressList.value = emptyList()
        _userAddress.value = UserAddress()
        _userProfile.value = UserProfile()
        clearCart()
    }
}
