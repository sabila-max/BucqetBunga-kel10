package com.example.bucqetbunga.models

import java.text.NumberFormat
import java.util.*
import com.example.bucqetbunga.BouquetCategory

data class Bouquet(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: BouquetCategory,
    val imageResId: Int
) {
    fun getFormattedPrice(): String {
        return "Rp ${"%,d".format(price.toInt()).replace(",", ".")}"
    }
}