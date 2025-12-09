package com.example.bucqetbunga.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bucqetbunga.R
import com.example.bucqetbunga.fragments.DashboardFragment
import com.example.bucqetbunga.fragments.CartFragment
import com.example.bucqetbunga.fragments.OrderFragment
import com.example.bucqetbunga.fragments.ProfileFragment
import com.example.bucqetbunga.utils.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi CartManager sebagai Class
        cartManager = CartManager(this)

        bottomNav = findViewById(R.id.bottomNavigation)

        bottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_home -> selectedFragment = DashboardFragment()
                R.id.nav_cart -> selectedFragment = CartFragment()
                R.id.nav_order -> selectedFragment = OrderFragment()
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }

            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
            }

            true
        }

        // Cek apakah ada intent untuk buka tab Pesanan
        val openOrders = intent.getBooleanExtra("open_orders", false)
        val openCart = intent.getBooleanExtra("open_cart", false)
        if (savedInstanceState == null) {
            if (openOrders) {
                // Buka tab Pesanan setelah checkout
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, OrderFragment())
                    .commit()
                bottomNav.selectedItemId = R.id.nav_order
            } else if (openCart) {
                // Buka tab Keranjang
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CartFragment())
                    .commit()
                bottomNav.selectedItemId = R.id.nav_cart
            } else {
                // Default: buka Dashboard
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, DashboardFragment())
                    .commit()
                bottomNav.selectedItemId = R.id.nav_home
            }
        }

        updateCartBadge()
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }

    // FIX: Mengubah CartManager.getCartCount() menjadi cartManager.getCartCount()
    fun updateCartBadge() {
        val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
        val cartCount = cartManager.getCartCount() // Menggunakan instance CartManager

        if (cartCount > 0) {
            badge.isVisible = true
            badge.number = cartCount
        } else {
            badge.isVisible = false
        }
    }
}