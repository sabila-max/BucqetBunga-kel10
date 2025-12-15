package com.example.bucqetbunga.models

import java.io.Serializable
import java.text.NumberFormat
import java.util.*
import com.example.bucqetbunga.BouquetCategory

data class Bouquet(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: BouquetCategory = BouquetCategory.ALL,
    val imageResId: Int = 0
) : Serializable {  // TAMBAHKAN: Implement Serializable
    fun getFormattedPrice(): String {
        return "Rp ${"%,d".format(price.toInt()).replace(",", ".")}"
    }
}