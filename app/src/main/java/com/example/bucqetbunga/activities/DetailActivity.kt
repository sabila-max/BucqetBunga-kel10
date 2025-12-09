package com.example.bucqetbunga.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bucqetbunga.R
import com.example.bucqetbunga.data.BouquetDataSource
import com.example.bucqetbunga.utils.CartManager

class DetailActivity : AppCompatActivity() {

    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        cartManager = CartManager(this)

        // Ambil ID yang dikirim dari Home
        val bouquetId = intent.getIntExtra("BOUQUET_ID", -1)
        if (bouquetId == -1) {
            finish()
            return
        }

        val bouquet = BouquetDataSource.getBouquets().find { it.id == bouquetId }

        if (bouquet != null) {
            // Setup View
            findViewById<ImageView>(R.id.ivDetailImage).setImageResource(bouquet.imageResId)
            findViewById<TextView>(R.id.tvDetailName).text = bouquet.name
            findViewById<TextView>(R.id.tvDetailPrice).text = bouquet.getFormattedPrice()
            findViewById<TextView>(R.id.tvDetailDesc).text = bouquet.description

            // Tampilkan kategori juga
            findViewById<TextView>(R.id.tvDetailCategory).text =
                "Kategori: ${bouquet.category.name.replace("_", " ")}"

            val etNote = findViewById<EditText>(R.id.etNote)

            // Button Keranjang
            findViewById<Button>(R.id.btnAddToCartDetail).setOnClickListener {
                val note = etNote.text.toString().trim()
                cartManager.addItemToCartWithNote(bouquet, note)
                Toast.makeText(this, "${bouquet.name} ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
                // Tidak perlu panggil MainActivity.updateCartBadge() dari sini
            }

            // Button Pesan Sekarang
            findViewById<Button>(R.id.btnBuyNowDetail).setOnClickListener {
                val note = etNote.text.toString().trim()
                cartManager.addItemToCartWithNote(bouquet, note)

                Toast.makeText(this, "${bouquet.name} ditambahkan! Menuju keranjang...", Toast.LENGTH_SHORT).show()

                // Pindah ke CartFragment di MainActivity
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    putExtra("open_cart", true)
                }
                startActivity(intent)
                finish()
            }

            // Tombol Kembali
            findViewById<ImageView>(R.id.ivBack).setOnClickListener {
                onBackPressed()
            }
        } else {
            Toast.makeText(this, "Produk tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Update badge jika kembali dari activity lain
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("update_badge", true)
    }
}