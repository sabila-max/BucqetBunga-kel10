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
        val selectedItems = CartManager.getSelectedItems()

        if (selectedItems.isNotEmpty()) {
            val firstItem = selectedItems[0]
            tvProductName.text = firstItem.bouquet.name
            tvProductPrice.text = CartManager.getFormattedTotal()
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

        // Buat pesanan
        val order = CartManager.createOrder(customerName, alamat, paymentMethod)

        Toast.makeText(
            this,
            "Pesanan berhasil dibuat!\nTotal: ${order.getFormattedTotal()}\nPembayaran: $paymentMethod",
            Toast.LENGTH_LONG
        ).show()

        // Kembali ke MainActivity dan pindah ke tab Pesanan
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("open_orders", true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}
