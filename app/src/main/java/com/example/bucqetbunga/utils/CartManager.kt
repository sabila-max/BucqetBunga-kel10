package com.example.bucqetbunga.utils

import android.content.Context
import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.models.CartItem
import com.example.bucqetbunga.models.Order
import com.example.bucqetbunga.models.OrderItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.*

class CartManager(private val context: Context) {

    private val PREF_NAME = "CartPrefs"
    private val KEY_CART_ITEMS = "CartItems"
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // --- CART MANAGEMENT ---

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

    fun addItemToCart(bouquet: Bouquet) {
        val currentItems = getCartItems()
        val existingItem = currentItems.find { it.bouquet.id == bouquet.id && it.note.isEmpty() }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
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

    fun updateCartItem(updatedItem: CartItem) {
        val currentItems = getCartItems()
        val index = currentItems.indexOfFirst { it.id == updatedItem.id }

        if (index != -1) {
            currentItems[index] = updatedItem
            saveCartItems(currentItems)
        }
    }

    fun removeFromCart(cartItemId: Long) {
        val currentItems = getCartItems()
        currentItems.removeAll { it.id == cartItemId }
        saveCartItems(currentItems)
    }

    fun removeFromCartByBouquetId(bouquetId: Int) {
        val currentItems = getCartItems()
        currentItems.removeAll { it.bouquet.id == bouquetId }
        saveCartItems(currentItems)
    }

    fun toggleItemSelection(cartItemId: Long) {
        val currentItems = getCartItems()
        val item = currentItems.find { it.id == cartItemId }
        item?.let {
            it.isSelected = !it.isSelected
            saveCartItems(currentItems)
        }
    }

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

    fun getCartCount(): Int {
        return getCartItems().sumOf { it.quantity }
    }

    fun clearSelectedItems() {
        val currentItems = getCartItems()
        val remainingItems = currentItems.filter { !it.isSelected }.toMutableList()
        saveCartItems(remainingItems)
    }

    fun clearCart() {
        saveCartItems(mutableListOf())
    }

    // --- ORDER CREATION ---

    fun createOrder(
        customerName: String,
        address: String,
        phone: String,
        note: String,
        paymentMethod: String,
        orderManager: OrderManager // Butuh OrderManager untuk generate ID
    ): Order {
        val selectedItems = getSelectedItems()
        val total = getTotalPrice()

        // Konversi CartItem ke OrderItem
        val orderItems = selectedItems.map { cartItem ->
            OrderItem(
                bouquet = cartItem.bouquet,
                quantity = cartItem.quantity,
                note = cartItem.note
            )
        }

        // Gunakan OrderManager untuk generate ID
        val orderId = orderManager.generateOrderId()

        val order = Order(
            id = orderId,
            items = orderItems,
            customerName = customerName,
            address = address,
            phone = phone,
            note = note,
            orderDate = System.currentTimeMillis(),
            totalAmount = total,
            paymentMethod = paymentMethod,
            status = "Menunggu Pembayaran"
        )

        return order
    }
}