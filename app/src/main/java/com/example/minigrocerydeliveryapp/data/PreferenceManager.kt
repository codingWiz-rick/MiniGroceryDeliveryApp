package com.example.minigrocerydeliveryapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.minigrocerydeliveryapp.model.Order
import com.example.minigrocerydeliveryapp.model.UserAddress
import com.example.minigrocerydeliveryapp.model.UserProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("grocery_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun saveUserProfile(profile: UserProfile) {
        sharedPreferences.edit().apply {
            putString("user_name", profile.name)
            putString("user_email", profile.email)
            putString("user_phone", profile.phone)
            apply()
        }
    }

    fun getUserProfile(): UserProfile {
        return UserProfile(
            name = sharedPreferences.getString("user_name", "John Doe") ?: "John Doe",
            email = sharedPreferences.getString("user_email", "john.doe@example.com") ?: "john.doe@example.com",
            phone = sharedPreferences.getString("user_phone", "") ?: ""
        )
    }

    fun saveAddress(address: UserAddress) {
        sharedPreferences.edit().apply {
            putString("addr_name", address.name)
            putString("addr_phone", address.phone)
            putString("addr_flat", address.flatHouseNo)
            putString("addr_area", address.areaStreet)
            putString("addr_landmark", address.landmark)
            putString("addr_city", address.city)
            putString("addr_pincode", address.pincode)
            apply()
        }
    }

    fun getAddress(): UserAddress {
        return UserAddress(
            name = sharedPreferences.getString("addr_name", "") ?: "",
            phone = sharedPreferences.getString("addr_phone", "") ?: "",
            flatHouseNo = sharedPreferences.getString("addr_flat", "") ?: "",
            areaStreet = sharedPreferences.getString("addr_area", "") ?: "",
            landmark = sharedPreferences.getString("addr_landmark", "") ?: "",
            city = sharedPreferences.getString("addr_city", "") ?: "",
            pincode = sharedPreferences.getString("addr_pincode", "") ?: ""
        )
    }

    fun saveAddressList(addresses: List<UserAddress>) {
        val json = gson.toJson(addresses)
        sharedPreferences.edit().putString("address_list", json).apply()
    }

    fun getAddressList(): List<UserAddress> {
        val json = sharedPreferences.getString("address_list", null) ?: return emptyList()
        val type = object : TypeToken<List<UserAddress>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveOrders(orders: List<Order>) {
        val json = gson.toJson(orders)
        sharedPreferences.edit().putString("orders_list", json).apply()
    }

    fun getOrders(): List<Order> {
        val json = sharedPreferences.getString("orders_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Order>>() {}.type
        return gson.fromJson(json, type)
    }

    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean("is_dark_mode", true)
    }

    fun setDarkMode(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", isDark).commit()
    }

    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", true)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    fun clearSession() {
        val isDark = isDarkMode()
        val notifications = areNotificationsEnabled()
        sharedPreferences.edit().clear().commit()
        setDarkMode(isDark)
        setNotificationsEnabled(notifications)
    }
}
