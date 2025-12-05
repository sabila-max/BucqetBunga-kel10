package com.example.bucqetbunga.models

data class Bouquet(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val stock: Int
) {
    fun getFormattedPrice(): String {
        return "Rp. ${String.format("%,.0f", price)}"
    }
}