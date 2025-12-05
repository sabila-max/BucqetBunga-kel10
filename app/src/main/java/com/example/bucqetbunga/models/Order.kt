package com.example.bucqetbunga.models

import java.text.SimpleDateFormat
import java.util.*

data class Order(
    val id: Int,
    val items: List<CartItem>,
    val customerName: String,
    val address: String,
    val paymentMethod: String,
    val total: Double,
    var status: String, // "Menunggu Pembayaran", "Diproses", "Dikirim", "Selesai"
    val orderDate: Long
) {
    fun getFormattedTotal(): String {
        return "Rp. ${String.format("%,.0f", total)}"
    }

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        return sdf.format(Date(orderDate))
    }

    fun getItemsSummary(): String {
        return items.joinToString(", ") { "${it.bouquet.name} (${it.quantity}x)" }
    }
}