package com.example.bucqetbunga.utils

import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.models.CartItem
import com.example.bucqetbunga.models.Order

object CartManager {
    private val cartItems = mutableListOf<CartItem>()
    private val orders = mutableListOf<Order>() // TAMBAHAN untuk menyimpan pesanan
    private var itemIdCounter = 1
    private var orderIdCounter = 1

    fun addToCart(bouquet: Bouquet) {
        // Cek apakah produk sudah ada di cart
        val existingItem = cartItems.find { it.bouquet.id == bouquet.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(itemIdCounter++, bouquet, 1, false))
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        cartItems.remove(cartItem)
    }

    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    fun getSelectedItems(): List<CartItem> {
        return cartItems.filter { it.isSelected }
    }

    fun getTotalPrice(): Double {
        return cartItems.filter { it.isSelected }.sumOf { it.getTotalPrice() }
    }

    fun getFormattedTotal(): String {
        return "Rp. ${String.format("%,.0f", getTotalPrice())}"
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getCartCount(): Int {
        return cartItems.size
    }

    // TAMBAHAN: Fungsi untuk membuat pesanan
    fun createOrder(customerName: String, address: String, paymentMethod: String): Order {
        val selectedItems = getSelectedItems()
        val total = getTotalPrice()

        val order = Order(
            id = orderIdCounter++,
            items = selectedItems.map { it.copy() },
            customerName = customerName,
            address = address,
            paymentMethod = paymentMethod,
            total = total,
            status = "Menunggu Pembayaran",
            orderDate = System.currentTimeMillis()
        )

        orders.add(order)

        // Hapus item yang sudah dipesan dari cart
        selectedItems.forEach { removeFromCart(it) }

        return order
    }

    fun getOrders(): List<Order> {
        return orders
    }
}