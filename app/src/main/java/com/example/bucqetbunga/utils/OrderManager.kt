package com.example.bucqetbunga.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.bucqetbunga.models.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "order_preferences"
        private const val KEY_ORDERS = "orders_list"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveOrder(order: Order) {
        val orders = getOrders().toMutableList()

        // Generate ID jika kosong
        val orderWithId = if (order.id.isEmpty()) {
            order.copy(id = generateOrderId())
        } else {
            order
        }

        orders.add(orderWithId)
        saveOrdersToPref(orders)
    }

    fun getOrders(): List<Order> {
        val json = sharedPref.getString(KEY_ORDERS, "[]") ?: "[]"
        val type = object : TypeToken<List<Order>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun updateOrder(updatedOrder: Order) {
        val orders = getOrders().toMutableList()
        val index = orders.indexOfFirst { it.id == updatedOrder.id }

        if (index != -1) {
            orders[index] = updatedOrder
            saveOrdersToPref(orders)
        }
    }

    fun deleteOrder(orderId: String) {
        val orders = getOrders().toMutableList()
        orders.removeAll { it.id == orderId }
        saveOrdersToPref(orders)
    }

    fun getOrderById(orderId: String): Order? {
        return getOrders().find { it.id == orderId }
    }

    fun getOrderCountByStatus(status: String): Int {
        return getOrders().count { it.status == status }
    }

    fun getOrdersByStatus(status: String): List<Order> {
        return getOrders().filter { it.status == status }
    }

    // PUBLIC: Untuk digunakan oleh CartManager atau lainnya
    fun generateOrderId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (1000..9999).random()
        return "ORD-$timestamp-$random"
    }

    private fun saveOrdersToPref(orders: List<Order>) {
        val json = gson.toJson(orders)
        sharedPref.edit().putString(KEY_ORDERS, json).apply()
    }
}