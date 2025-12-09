package com.example.bucqetbunga.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bucqetbunga.R
import com.example.bucqetbunga.utils.CartManager

class CheckoutActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var etNoPesanan: EditText
    private lateinit var etAlamat: EditText
    private lateinit var rgPayment: RadioGroup
    private lateinit var btnOrder: Button
    private lateinit var cartManager: CartManager

    companion object {
        private const val TAG = "CheckoutActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        try {
            setContentView(R.layout.activity_checkout)
            cartManager = CartManager(this)

            initViews()
            loadCheckoutData()
            setupListeners()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        tvProductName = findViewById(R.id.tvProductName)
        tvProductPrice = findViewById(R.id.tvProductPrice)
        etNoPesanan = findViewById(R.id.etNoPesanan)
        etAlamat = findViewById(R.id.etAlamat)
        rgPayment = findViewById(R.id.rgPayment)
        btnOrder = findViewById(R.id.btnOrder)
    }

    private fun loadCheckoutData() {
        try {
            val selectedItems = cartManager.getSelectedItems()
            Log.d(TAG, "Selected items count: ${selectedItems.size}")

            if (selectedItems.isNotEmpty()) {
                val itemNames = selectedItems.joinToString(", ") { it.bouquet.name }
                tvProductName.text = itemNames
                tvProductPrice.text = cartManager.getFormattedTotal()
                Log.d(TAG, "Checkout data loaded: $itemNames")
            } else {
                Log.w(TAG, "No selected items found")
                Toast.makeText(this, "Tidak ada item yang dipilih", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading checkout data: ${e.message}", e)
        }
    }

    private fun setupListeners() {
        ivBack.setOnClickListener {
            finish()
        }

        btnOrder.setOnClickListener {
            processOrder()
        }
    }

    private fun processOrder() {
        try {
            val customerName = etNoPesanan.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val selectedPaymentId = rgPayment.checkedRadioButtonId

            // Validasi
            if (customerName.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return
            }

            if (selectedPaymentId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
                return
            }

            val paymentMethod = findViewById<RadioButton>(selectedPaymentId).text.toString()

            Log.d(TAG, "Processing order for: $customerName")

            // Buat pesanan
            val order = cartManager.createOrder(customerName, alamat, paymentMethod)

            Log.d(TAG, "Order created successfully: ${order.id}")

            Toast.makeText(
                this,
                "Pesanan berhasil dibuat!\nTotal: ${order.getFormattedTotal()}\nPembayaran: $paymentMethod",
                Toast.LENGTH_LONG
            ).show()

            // Kembali ke MainActivity dan pindah ke tab Pesanan
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("open_orders", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "Error processing order: ${e.message}", e)
            Toast.makeText(this, "Gagal membuat pesanan: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}