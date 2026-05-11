package com.example.minigrocerydeliveryapp.model

data class UserAddress(
    val name: String = "",
    val phone: String = "",
    val flatHouseNo: String = "",
    val areaStreet: String = "",
    val landmark: String = "",
    val city: String = "",
    val pincode: String = ""
) {
    fun toDisplayString(): String {
        return if (flatHouseNo.isBlank()) "Set Location"
        else "$flatHouseNo, $areaStreet, $city - $pincode"
    }

    fun isComplete(): Boolean {
        return name.isNotBlank() && flatHouseNo.isNotBlank() && city.isNotBlank() && pincode.isNotBlank()
    }
}
