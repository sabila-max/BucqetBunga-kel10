package com.example.bucqetbunga.activities

import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        try {
            setContentView(R.layout.activity_main)

            cartManager = CartManager(this)
            bottomNav = findViewById(R.id.bottomNavigation)

            setupBottomNavigation()

            // Cek apakah ada intent untuk buka tab Pesanan
            val openOrders = intent.getBooleanExtra("open_orders", false)
            Log.d(TAG, "Open orders intent: $openOrders")

            if (savedInstanceState == null) {
                if (openOrders) {
                    Log.d(TAG, "Opening OrderFragment from intent")
                    loadFragment(OrderFragment())
                    bottomNav.selectedItemId = R.id.nav_order
                } else {
                    Log.d(TAG, "Opening DashboardFragment (default)")
                    loadFragment(DashboardFragment())
                    bottomNav.selectedItemId = R.id.nav_home
                }
            }

            updateCartBadge()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
        }
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_home -> {
                    Log.d(TAG, "DashboardFragment selected")
                    selectedFragment = DashboardFragment()
                }
                R.id.nav_cart -> {
                    Log.d(TAG, "CartFragment selected")
                    selectedFragment = CartFragment()
                }
                R.id.nav_order -> {
                    Log.d(TAG, "OrderFragment selected")
                    selectedFragment = OrderFragment()
                }
                R.id.nav_profile -> {
                    Log.d(TAG, "ProfileFragment selected")
                    selectedFragment = ProfileFragment()
                }
            }

            selectedFragment?.let {
                loadFragment(it)
            }

            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            Log.d(TAG, "Fragment loaded: ${fragment::class.java.simpleName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading fragment: ${e.message}", e)
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }

    fun updateCartBadge() {
        try {
            val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
            val cartCount = cartManager.getCartCount()

            if (cartCount > 0) {
                badge.isVisible = true
                badge.number = cartCount
            } else {
                badge.isVisible = false
            }
            Log.d(TAG, "Cart badge updated: $cartCount items")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating cart badge: ${e.message}", e)
        }
    }
}