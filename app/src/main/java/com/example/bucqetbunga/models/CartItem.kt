package com.example.bucqetbunga.models

import java.text.NumberFormat
import java.util.*

data class CartItem(
    val id: Long,
    val bouquet: Bouquet,
    var quantity: Int = 1,
    var isSelected: Boolean = true,
    var note: String = "" // <-- TAMBAHAN: Field untuk catatan
) {
    fun getTotalPrice(): Double {
        return bouquet.price * quantity
    }

    fun getFormattedTotal(): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(getTotalPrice())
    }
}