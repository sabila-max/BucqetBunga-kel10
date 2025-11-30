package com.example.bucqetbunga.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bucqetbunga.R
import com.example.bucqetbunga.fragments.DashboardFragment
import com.example.bucqetbunga.fragments.CartFragment
import com.example.bucqetbunga.fragments.OrderFragment
import com.example.bucqetbunga.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigation)

        bottomNav.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_home -> selectedFragment = DashboardFragment()
                R.id.nav_cart -> selectedFragment = CartFragment()
                R.id.nav_order -> selectedFragment = OrderFragment()  // TAMBAHAN INI
                R.id.nav_profile -> selectedFragment = ProfileFragment()
            }

            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
            }

            true
        }

        // Load default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DashboardFragment())
                .commit()
            bottomNav.selectedItemId = R.id.nav_home
        }
    }
}