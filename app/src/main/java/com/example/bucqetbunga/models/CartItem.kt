package com.example.bucqetbunga.models

data class CartItem(
    val id: Int,
    val bouquet: Bouquet,
    var quantity: Int = 1,
    var isSelected: Boolean = false
) {
    fun getTotalPrice(): Double {
        return bouquet.price * quantity
    }

    fun getFormattedTotal(): String {
        return "Rp. ${String.format("%,.0f", getTotalPrice())}"
    }
}