package com.example.bucqetbunga.activities

import android.content.Intent
import android.os.Bundle
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

    // 1. Deklarasi Instance CartManager
    private lateinit var cartManager: CartManager

    private lateinit var ivBack: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var etNoPesanan: EditText
    private lateinit var etAlamat: EditText
    private lateinit var rgPayment: RadioGroup
    private lateinit var btnOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // 2. Inisialisasi CartManager
        cartManager = CartManager(this)

        initViews()
        loadCheckoutData()
        setupListeners()
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
        // 3. Panggil fungsi pakai huruf kecil 'cartManager'
        val selectedItems = cartManager.getSelectedItems()

        if (selectedItems.isNotEmpty()) {
            val firstItem = selectedItems[0]
            // Jika lebih dari 1 item, tampilkan "Item A, Item B, dll"
            if (selectedItems.size > 1) {
                tvProductName.text = "${firstItem.bouquet.name} dan ${selectedItems.size - 1} lainnya"
            } else {
                tvProductName.text = firstItem.bouquet.name
            }
            tvProductPrice.text = cartManager.getFormattedTotal()
        }
    }

    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
        btnOrder.setOnClickListener { processOrder() }
    }

    private fun processOrder() {
        val customerName = etNoPesanan.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val selectedPaymentId = rgPayment.checkedRadioButtonId

        if (customerName.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedPaymentId == -1) {
            Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = findViewById<RadioButton>(selectedPaymentId).text.toString()

        // 4. Panggil createOrder dari instance
        val order = cartManager.createOrder(customerName, alamat, paymentMethod)

        Toast.makeText(
            this,
            "Pesanan berhasil!\nTotal: ${order.getFormattedTotal()}",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("open_orders", true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}