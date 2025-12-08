// File: Bouquet.kt
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
    val stock: Int,
    val imageResId: Int
) {
    fun getFormattedPrice(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(price)
    }
}