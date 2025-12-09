package com.example.bucqetbunga.activities

import android.content.Intent // <-- FIX: Import Intent yang Hilang
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

            val etNote = findViewById<EditText>(R.id.etNote)

            // Button Keranjang
            findViewById<Button>(R.id.btnAddToCartDetail).setOnClickListener {
                val note = etNote.text.toString()
                cartManager.addItemToCartWithNote(bouquet, note)
                Toast.makeText(this, "Masuk Keranjang (+Catatan)", Toast.LENGTH_SHORT).show()
                // FIX: Panggil update badge
                (applicationContext as? MainActivity)?.updateCartBadge()
            }

            // Button Pesan Sekarang
            findViewById<Button>(R.id.btnBuyNowDetail).setOnClickListener {
                val note = etNote.text.toString()
                cartManager.addItemToCartWithNote(bouquet, note)

                // Pindah ke CartFragment di MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("open_cart", true) // Sinyal untuk membuka CartFragment
                startActivity(intent)
                finish()
            }
        }
    }
}