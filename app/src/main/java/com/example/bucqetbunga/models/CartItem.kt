package com.example.bucqetbunga.models

import java.text.NumberFormat
import java.util.*

// FIX: Struktur disederhanakan untuk persistence. Bouquet.id digunakan sebagai pengenal.
data class CartItem(
    val bouquet: Bouquet,
    var quantity: Int = 1,
    var isSelected: Boolean = true // Default set True saat item ditambahkan
) {
    fun getTotalPrice(): Double {
        return bouquet.price * quantity
    }

    fun getFormattedTotal(): String {
        // Menggunakan format Rupiah yang benar dan modern
        val indonesiaLocale = Locale.forLanguageTag("in-ID")
        val formatter = NumberFormat.getCurrencyInstance(indonesiaLocale)
        formatter.maximumFractionDigits = 0
        return formatter.format(getTotalPrice())
    }
}