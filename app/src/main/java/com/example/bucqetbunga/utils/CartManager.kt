package com.example.bucqetbunga.utils

import android.content.Context
import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.models.CartItem
import com.example.bucqetbunga.models.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.*

class CartManager(private val context: Context) {

    private val PREF_NAME = "CartPrefs"
    private val KEY_CART_ITEMS = "CartItems"
    private val KEY_ORDERS = "Orders"
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // --- KERANJANG UTAMA (Persistence Logic) ---

    fun getCartItems(): MutableList<CartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    private fun saveCartItems(items: MutableList<CartItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }

    // FIX: Quick Add (Dari Dashboard)
    fun addItemToCart(bouquet: Bouquet) {
        val currentItems = getCartItems()
        val existingItem = currentItems.find { it.bouquet.id == bouquet.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            // FIX: Menggunakan constructor CartItem yang lengkap (ID, Bouquet, Qty, isSelected, Note)
            currentItems.add(CartItem(
                id = System.currentTimeMillis(),
                bouquet = bouquet,
                quantity = 1,
                isSelected = true,
                note = ""
            ))
        }
        saveCartItems(currentItems)
    }

    // FIX: Add With Note (Dari DetailActivity)
    fun addItemToCartWithNote(bouquet: Bouquet, note: String) {
        val currentItems = getCartItems()
        val existingItem = currentItems.find {
            it.bouquet.id == bouquet.id && it.note == note
        }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentItems.add(CartItem(
                id = System.currentTimeMillis(),
                bouquet = bouquet,
                quantity = 1,
                isSelected = true,
                note = note
            ))
        }
        saveCartItems(currentItems)
    }

    // FIX: Fungsi Checkout
    fun createOrder(customerName: String, address: String, paymentMethod: String): Order {
        val selectedItems = getSelectedItems()
        val total = getTotalPrice()

        val order = Order(
            id = System.currentTimeMillis(), // FIX: ID harus Long agar sesuai dengan Order.kt
            items = selectedItems.map { it.copy() },
            customerName = customerName,
            address = address,
            paymentMethod = paymentMethod,
            total = total,
            status = "Menunggu Pembayaran",
            orderDate = System.currentTimeMillis()
        )

        val currentOrders = getOrders()
        currentOrders.add(order)
        prefs.edit().putString(KEY_ORDERS, gson.toJson(currentOrders)).apply()

        val remainingItems = getCartItems().filter { !it.isSelected }.toMutableList()
        saveCartItems(remainingItems)

        return order
    }

    // ... (Fungsi helper lainnya)

    fun getSelectedItems(): List<CartItem> {
        return getCartItems().filter { it.isSelected }
    }

    fun getTotalPrice(): Double {
        return getSelectedItems().sumOf { it.bouquet.price * it.quantity }
    }

    fun getFormattedTotal(): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(getTotalPrice())
    }

    fun getOrders(): MutableList<Order> {
        val json = prefs.getString(KEY_ORDERS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Order>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    fun getCartCount(): Int {
        return getCartItems().sumOf { it.quantity }
    }
}