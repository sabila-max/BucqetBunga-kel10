package com.example.bucqetbunga.models

import java.text.NumberFormat
import java.util.*

data class Bouquet(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double, // Menggunakan Double untuk kalkulasi
    val category: BouquetCategory, // Menggunakan Enum Category
    val stock: Int,
    val imageResId: Int // ID resource gambar (R.drawable.xxx)
) {
    // Fungsi untuk memformat harga menjadi Rupiah (reusable)
    fun getFormattedPrice(): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.maximumFractionDigits = 0 // Menghilangkan ,00 di belakang
        return formatter.format(price) // Contoh output: Rp125.000
    }
}