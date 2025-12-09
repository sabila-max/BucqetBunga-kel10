package com.example.bucqetbunga.models

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class Order(
    // FIX: Menggunakan Long untuk ID yang lebih umum
    val id: Long,
    val items: List<CartItem>,
    val customerName: String,
    val address: String,
    val paymentMethod: String,
    val total: Double,
    val status: String,
    val orderDate: Long
) {
    fun getFormattedTotal(): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(total)
    }

    // FIX: Fungsi untuk format tanggal (Digunakan di OrderAdapter)
    fun getFormattedDate(): String {
        val date = Date(orderDate)
        val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("in", "ID"))
        return format.format(date)
    }

    // FIX: Fungsi untuk membuat ringkasan item (Digunakan di OrderAdapter)
    fun getItemsSummary(): String {
        // Gabungkan nama item dan kuantitasnya
        return items.joinToString(", ") { "${it.bouquet.name} x${it.quantity}" }
    }
}