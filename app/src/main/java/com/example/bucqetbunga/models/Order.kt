package com.example.bucqetbunga.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Order(
    var id: String = "",
    var customerName: String = "",
    var address: String = "",
    var phone: String = "",
    var note: String = "",
    var items: List<OrderItem> = emptyList(),
    var orderDate: Long = System.currentTimeMillis(),
    var totalAmount: Double = 0.0,
    var paymentMethod: String = "",
    var status: String = "Menunggu Pembayaran"
) : Serializable {

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(orderDate))
    }

    fun getFormattedTotal(): String {
        return "Rp ${"%,d".format(totalAmount.toInt()).replace(",", ".")}"
    }

    fun getTotalItemCount(): Int {
        return items.sumOf { it.quantity }
    }

    fun getStatusWithColor(): Pair<String, Int> {
        return when (status) {
            "Menunggu Pembayaran" -> "Menunggu Pembayaran" to 0xFFFF9800.toInt()
            "Diproses" -> "Diproses" to 0xFF2196F3.toInt()
            "Dikirim" -> "Dikirim" to 0xFF4CAF50.toInt()
            "Selesai" -> "Selesai" to 0xFF4CAF50.toInt()
            "Dibatalkan" -> "Dibatalkan" to 0xFFF44336.toInt()
            else -> status to 0xFF9E9E9E.toInt()
        }
    }

    fun getItemsSummary(): String {
        return when {
            items.size > 1 -> "${items.first().bouquet.name} dan ${items.size - 1} item lainnya"
            items.isNotEmpty() -> items.first().bouquet.name
            else -> "Tidak ada item"
        }
    }

    fun recalculateTotal() {
        totalAmount = items.sumOf { it.getSubtotal() }
    }
}
