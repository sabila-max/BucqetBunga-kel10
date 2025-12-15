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

/**
 CartManager bertugas mengelola seluruh data keranjang belanja.
 Data disimpan di SharedPreferences dalam bentuk JSON menggunakan Gson.
 Fungsinya meliputi:
 - tambah item
 - ubah quantity
 - hapus item
 - hitung total
 - pilih/unselect item
 - membuat order dari item terpilih
 */
class CartManager(private val context: Context) {

    // Nama file SharedPreferences
    private val PREF_NAME = "CartPrefs"

    // Key penyimpanan list item cart
    private val KEY_CART_ITEMS = "CartItems"

    // Instance SharedPreferences
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    // FUNGSI MANAJEMEN CART

    /**
     Mengambil semua item keranjang dalam bentuk MutableList<CartItem>.
     Data JSON akan di-convert kembali menjadi objek menggunakan Gson.
     */
    fun getCartItems(): MutableList<CartItem> {
        val json = prefs.getString(KEY_CART_ITEMS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<CartItem>>() {}.type
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    /**
     Menyimpan kembali list cart ke SharedPreferences sebagai JSON.
     */
    private fun saveCartItems(items: MutableList<CartItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }

    /**
     Menambah item bouquet ke keranjang tanpa catatan.
     Jika item sudah ada dan tidak punya catatan, quantity ditambah.
     */
    fun addItemToCart(bouquet: Bouquet) {
        val currentItems = getCartItems()
        val existingItem = currentItems.find { it.bouquet.id == bouquet.id && it.note.isEmpty() }

        if (existingItem != null) {
            existingItem.quantity += 1 // Tambah qty jika item sudah ada
        } else {
            // Tambah item baru
            currentItems.add(
                CartItem(
                    id = System.currentTimeMillis(), // ID unik
                    bouquet = bouquet,
                    quantity = 1,
                    isSelected = true,
                    note = ""
                )
            )
        }
        saveCartItems(currentItems)
    }

    /**
     Menambah item bouquet ke keranjang dengan catatan.
     Jika item + catatan sama, maka quantity ditambah.
     */
    fun addItemToCartWithNote(bouquet: Bouquet, note: String) {
        val currentItems = getCartItems()
        val existingItem = currentItems.find {
            it.bouquet.id == bouquet.id && it.note == note
        }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            currentItems.add(
                CartItem(
                    id = System.currentTimeMillis(),
                    bouquet = bouquet,
                    quantity = 1,
                    isSelected = true,
                    note = note
                )
            )
        }
        saveCartItems(currentItems)
    }

    /**
     Mengupdate item di keranjang berdasarkan ID.
     Biasanya dipakai setelah user klik plus/minus/checkbox.
     */
    fun updateCartItem(updatedItem: CartItem) {
        val currentItems = getCartItems()
        val index = currentItems.indexOfFirst { it.id == updatedItem.id }

        if (index != -1) {
            currentItems[index] = updatedItem
            saveCartItems(currentItems)
        }
    }

    /**
     Menghapus item dari keranjang berdasarkan cartItemId.
     */
    fun removeFromCart(cartItemId: Long) {
        val currentItems = getCartItems()
        currentItems.removeAll { it.id == cartItemId }
        saveCartItems(currentItems)
    }

    /**
     Menghapus item berdasarkan ID bouquet.
     Digunakan jika ingin menghapus semua variasi item dari produk yang sama.
     */
    fun removeFromCartByBouquetId(bouquetId: Int) {
        val currentItems = getCartItems()
        currentItems.removeAll { it.bouquet.id == bouquetId }
        saveCartItems(currentItems)
    }

    /**
     Mengubah status checkbox (selected / unselected).
     */
    fun toggleItemSelection(cartItemId: Long) {
        val currentItems = getCartItems()
        val item = currentItems.find { it.id == cartItemId }
        item?.let {
            it.isSelected = !it.isSelected
            saveCartItems(currentItems)
        }
    }

    /**
     Mengambil hanya item yang dipilih (isSelected = true).
     */
    fun getSelectedItems(): List<CartItem> {
        return getCartItems().filter { it.isSelected }
    }

    /**
     Menghitung total harga dari item yang dipilih untuk checkout.
     */
    fun getTotalPrice(): Double {
        return getSelectedItems().sumOf { it.bouquet.price * it.quantity }
    }

    /**
     Mengubah total harga menjadi format rupiah, contoh: Rp50.000.
     */
    fun getFormattedTotal(): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        formatter.maximumFractionDigits = 0
        return formatter.format(getTotalPrice())
    }

    /**
     Menghitung jumlah total item (quantity), bukan jumlah baris item.
     Dipakai untuk badge jumlah item di icon cart.
     */
    fun getCartCount(): Int {
        return getCartItems().sumOf { it.quantity }
    }

    /**
     Menghapus hanya item yang dipilih untuk checkout.
     Digunakan setelah selesai order.
     */
    fun clearSelectedItems() {
        val currentItems = getCartItems()
        val remainingItems = currentItems.filter { !it.isSelected }.toMutableList()
        saveCartItems(remainingItems)
    }

    /**
     Menghapus seluruh isi keranjang.
     */
    fun clearCart() {
        saveCartItems(mutableListOf())
    }

    // -------------------------------------------------------------------------
    //                               ORDER CREATION
    // -------------------------------------------------------------------------

    /**
     Membuat objek Order berdasarkan item terpilih.
     Digunakan di halaman Checkout.
     */
    fun createOrder(
        customerName: String,
        address: String,
        phone: String,
        note: String,
        paymentMethod: String,
        orderManager: OrderManager // dibutuhkan untuk generate ID order
    ): Order {

        val selectedItems = getSelectedItems()
        val total = getTotalPrice()

        // Konversi CartItem menjadi OrderItem (data lebih sederhana untuk riwayat)
        val orderItems = selectedItems.map { cartItem ->
            OrderItem(
                bouquet = cartItem.bouquet,
                quantity = cartItem.quantity,
                note = cartItem.note
            )
        }

        // Generate ID unik menggunakan OrderManager
        val orderId = orderManager.generateOrderId()

        // Buat objek Order untuk disimpan
        return Order(
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
    }
}
