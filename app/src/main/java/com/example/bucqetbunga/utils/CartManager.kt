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

    // --- UTILITIES PERSISTENSI ---

    fun getCartItems(): MutableList<CartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    fun saveCartItems(items: MutableList<CartItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }

    // --- LOGIKA KERANJANG ---

    fun addItemToCart(bouquet: Bouquet) {
        val currentItems = getCartItems()
        val existingItem = currentItems.find { it.bouquet.id == bouquet.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentItems.add(CartItem(bouquet = bouquet, quantity = 1, isSelected = true))
        }

        saveCartItems(currentItems)
    }

    fun updateItem(cartItem: CartItem) {
        val currentItems = getCartItems()
        val index = currentItems.indexOfFirst { it.bouquet.id == cartItem.bouquet.id }
        if (index != -1) {
            currentItems[index] = cartItem
            saveCartItems(currentItems)
        }
    }

    fun removeItemFromCart(cartItem: CartItem) {
        val currentItems = getCartItems()
        currentItems.removeIf { it.bouquet.id == cartItem.bouquet.id }
        saveCartItems(currentItems)
    }

    fun getSelectedItems(): List<CartItem> {
        return getCartItems().filter { it.isSelected }
    }

    fun getTotalPrice(): Double {
        return getCartItems().filter { it.isSelected }.sumOf { it.getTotalPrice() }
    }

    fun getFormattedTotal(): String {
        val indonesiaLocale = Locale.forLanguageTag("in-ID")
        val format = NumberFormat.getCurrencyInstance(indonesiaLocale)
        format.maximumFractionDigits = 0
        return format.format(getTotalPrice())
    }

    fun clearCart() {
        prefs.edit().remove(KEY_CART_ITEMS).apply()
    }

    fun getCartCount(): Int {
        return getCartItems().sumOf { it.quantity }
    }

    // --- LOGIKA ORDER/PESANAN ---

    fun getOrders(): MutableList<Order> {
        val json = prefs.getString(KEY_ORDERS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Order>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    fun createOrder(customerName: String, address: String, paymentMethod: String): Order {
        val selectedItems = getSelectedItems()
        val total = getTotalPrice()

        val order = Order(
            id = System.currentTimeMillis().hashCode(),
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

        // Hapus item yang sudah di-checkout dari keranjang
        val remainingItems = getCartItems().filter { !it.isSelected }.toMutableList()
        saveCartItems(remainingItems)

        return order
    }
}