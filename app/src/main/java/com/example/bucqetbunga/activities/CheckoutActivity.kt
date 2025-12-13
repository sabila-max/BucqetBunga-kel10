package com.example.bucqetbunga.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.bucqetbunga.R
import com.example.bucqetbunga.utils.CartManager
import com.example.bucqetbunga.utils.OrderManager

class CheckoutActivity : AppCompatActivity() {

    private lateinit var cartManager: CartManager
    private lateinit var orderManager: OrderManager
    private lateinit var ivBack: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvTotal: TextView
    private lateinit var etNamaPenerima: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHP: EditText
    private lateinit var etCatatan: EditText
    private lateinit var rgPayment: RadioGroup
    private lateinit var btnOrder: Button
    private lateinit var btnWhatsApp: Button

    private var lastOrderId: String = ""
    private var orderCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Inisialisasi managers
        cartManager = CartManager(this)
        orderManager = OrderManager(this)

        initViews()
        loadCheckoutData()
        setupListeners()

        // Setup OnBackPressedCallback untuk Android baru
        setupOnBackPressed()
    }

    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        tvProductName = findViewById(R.id.tvProductName)
        tvTotal = findViewById(R.id.tvTotal)
        etNamaPenerima = findViewById(R.id.etNamaPenerima)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHP = findViewById(R.id.etNoHP)
        etCatatan = findViewById(R.id.etCatatan)
        rgPayment = findViewById(R.id.rgPayment)
        btnOrder = findViewById(R.id.btnOrder)
        btnWhatsApp = findViewById(R.id.btnWhatsApp)

        // Awalnya nonaktif WhatsApp button
        btnWhatsApp.isEnabled = false
        btnWhatsApp.alpha = 0.5f
    }

    private fun setupOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (orderCompleted) {
                    // Jika order sudah selesai, kembali ke halaman Order
                    navigateToOrderPage()
                } else {
                    // Jika belum checkout, langsung kembali
                    finish()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showExitConfirmation() {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Lihat Pesanan")
            .setMessage("Pesanan sudah berhasil dibuat. Ingin melihat detail pesanan?")
            .setPositiveButton("Ya") { _, _ ->
                navigateToOrderPage()
            }
            .setNegativeButton("Nanti", null)
            .create()

        dialog.show()
    }

    private fun loadCheckoutData() {
        val selectedItems = cartManager.getSelectedItems()

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Keranjang kosong", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (selectedItems.size == 1) {
            tvProductName.text = selectedItems[0].bouquet.name
        } else {
            tvProductName.text = "${selectedItems[0].bouquet.name} dan ${selectedItems.size - 1} lainnya"
        }

        tvTotal.text = cartManager.getFormattedTotal()
    }

    private fun setupListeners() {
        ivBack.setOnClickListener {
            if (orderCompleted) {
                navigateToOrderPage()
            } else {
                finish()
            }
        }

        btnOrder.setOnClickListener {
            processOrder()
        }

        btnWhatsApp.setOnClickListener {
            if (lastOrderId.isNotEmpty()) {
                orderManager.getOrderById(lastOrderId)?.let { order ->
                    sendToWhatsApp(order)
                } ?: run {
                    Toast.makeText(this, "Order tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun processOrder() {
        // Validasi input
        val customerName = etNamaPenerima.text.toString().trim()
        val address = etAlamat.text.toString().trim()
        val phone = etNoHP.text.toString().trim()
        val note = etCatatan.text.toString().trim()
        val selectedPaymentId = rgPayment.checkedRadioButtonId

        if (customerName.isEmpty()) {
            etNamaPenerima.error = "Nama penerima harus diisi"
            etNamaPenerima.requestFocus()
            return
        }

        if (address.isEmpty()) {
            etAlamat.error = "Alamat harus diisi"
            etAlamat.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            etNoHP.error = "Nomor HP harus diisi"
            etNoHP.requestFocus()
            return
        }

        if (selectedPaymentId == -1) {
            Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentMethod = findViewById<RadioButton>(selectedPaymentId).text.toString()

        // Buat order
        val order = cartManager.createOrder(
            customerName = customerName,
            address = address,
            phone = phone,
            note = note,
            paymentMethod = paymentMethod,
            orderManager = orderManager
        )

        // Simpan order ke OrderManager
        orderManager.saveOrder(order)

        // Simpan ID order untuk WhatsApp
        lastOrderId = order.id

        // Hapus item yang sudah dipesan dari keranjang
        cartManager.clearSelectedItems()

        // Tampilkan konfirmasi
        Toast.makeText(this,
            "Pesanan berhasil dibuat!\nID: #${order.id.takeLast(6)}",
            Toast.LENGTH_LONG
        ).show()

        // Update UI
        btnOrder.text = "✅ Pesanan Berhasil"
        btnOrder.isEnabled = false
        btnWhatsApp.isEnabled = true
        btnWhatsApp.alpha = 1f
        orderCompleted = true

        // Nonaktifkan input
        etNamaPenerima.isEnabled = false
        etAlamat.isEnabled = false
        etNoHP.isEnabled = false
        etCatatan.isEnabled = false
        rgPayment.isEnabled = false

        for (i in 0 until rgPayment.childCount) {
            rgPayment.getChildAt(i).isEnabled = false
        }

        // PERUBAHAN UTAMA: Langsung navigasi ke halaman Order setelah 1.5 detik
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            navigateToOrderPage()
        }, 1500) // Delay 1.5 detik agar user sempat membaca toast
    }

    private fun navigateToOrderPage() {
        val intent = Intent(this, MainActivity::class.java).apply {
            // Clear semua activity di atasnya
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // Tambahkan flag untuk membuka tab Order
            putExtra("open_orders", true)
        }
        startActivity(intent)
        finish()
    }

    private fun sendToWhatsApp(order: com.example.bucqetbunga.models.Order) {
        val message = buildOrderMessage(order)
        val phoneNumber = "6281234567890" // Ganti dengan nomor admin toko bunga

        val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this,
                "WhatsApp tidak terinstall atau terjadi error",
                Toast.LENGTH_SHORT
            ).show()

            // Alternatif: copy ke clipboard
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Order Message", message)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this,
                "Pesan order disalin ke clipboard",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun buildOrderMessage(order: com.example.bucqetbunga.models.Order): String {
        val itemsText = order.items.joinToString("\n") { item ->
            "• ${item.bouquet.name} x${item.quantity} = ${item.getFormattedSubtotal()}"
        }

        return """
            Halo, saya ingin memesan buket bunga:
            
            *DETAIL PESANAN*
            ID Order: ${order.id}
            Tanggal: ${order.getFormattedDate()}
            
            *DATA PENERIMA*
            Nama: ${order.customerName}
            Alamat: ${order.address}
            No. HP: ${order.phone}
            Catatan: ${if (order.note.isEmpty()) "-" else order.note}
            
            *ITEM PESANAN:*
            $itemsText
            
            *TOTAL PEMBAYARAN:*
            ${order.getFormattedTotal()}
            
            *METODE PEMBAYARAN:*
            ${order.paymentMethod}
            
            Mohon konfirmasi ketersediaan dan total yang harus dibayar.
            Terima kasih!
        """.trimIndent()
    }

}