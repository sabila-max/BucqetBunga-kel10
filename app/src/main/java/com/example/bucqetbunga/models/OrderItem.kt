package com.example.bucqetbunga.models

import java.io.Serializable

data class OrderItem(
    var bouquet: Bouquet = Bouquet(),
    var quantity: Int = 1,
    var note: String = ""
) : Serializable {

    fun getSubtotal(): Double {
        return bouquet.price * quantity
    }

    fun getFormattedSubtotal(): String {
        return formatRupiah(getSubtotal())
    }

    fun getFormattedPrice(): String {
        return formatRupiah(bouquet.price)
    }

    private fun formatRupiah(amount: Double): String {
        return "Rp ${"%,d".format(amount.toInt()).replace(",", ".")}"
    }

    fun getItemSummary(): String {
        return "${bouquet.name} x$quantity"
    }

    override fun toString(): String {
        return "OrderItem(${bouquet.name}, qty=$quantity, note='$note')"
    }
}
