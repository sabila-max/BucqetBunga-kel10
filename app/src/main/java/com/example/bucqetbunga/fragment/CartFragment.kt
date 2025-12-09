package com.example.bucqetbunga.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.activities.CheckoutActivity
import com.example.bucqetbunga.activities.MainActivity
import com.example.bucqetbunga.adapters.CartAdapter
import com.example.bucqetbunga.utils.CartManager

class CartFragment : Fragment() {

    private lateinit var rvCart: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var emptyState: LinearLayout
    private lateinit var cartManager: CartManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        // FIX: Inisialisasi CartManager sebagai class
        cartManager = CartManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerView()
        updateUI() // updateUI dipanggil di onResume dan setelah setup RecyclerView
    }

    override fun onResume() {
        super.onResume()
        updateUI() // Memastikan UI diperbarui setiap kali Fragment kembali ke tampilan
        (activity as? MainActivity)?.updateCartBadge() // Update badge di BottomNav
    }

    private fun initViews(view: View) {
        // ID INI SEKARANG DIPASTIKAN ADA DI fragment_cart.xml
        rvCart = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        emptyState = view.findViewById(R.id.emptyState)
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(requireContext(), cartManager.getCartItems().toMutableList()) {
            updateTotal() // Callback dari adapter saat item/jumlah/pilihan berubah
            updateUI()
            (activity as? MainActivity)?.updateCartBadge()
        }

        rvCart.layoutManager = LinearLayoutManager(context) // Vertical list
        rvCart.adapter = adapter

        // Click listener untuk checkout
        btnCheckout.setOnClickListener {
            val selectedItems = cartManager.getSelectedItems()
            if (selectedItems.isNotEmpty()) {
                val intent = Intent(activity, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Pilih minimal satu item untuk Checkout!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        val cartItems = cartManager.getCartItems()

        if (cartItems.isEmpty()) {
            // Tampilkan empty state
            emptyState.visibility = View.VISIBLE
            rvCart.visibility = View.GONE
            tvTotal.text = cartManager.getFormattedTotal() // Menampilkan Rp0
            btnCheckout.isEnabled = false
            btnCheckout.text = "Checkout"
        } else {
            // Tampilkan empty state
            emptyState.visibility = View.GONE
            rvCart.visibility = View.VISIBLE
            adapter.updateList(cartItems)
            updateTotal()
        }
    }

    private fun updateTotal() {
        tvTotal.text = cartManager.getFormattedTotal()

        val selectedCount = cartManager.getSelectedItems().size
        val isAnyItemSelected = selectedCount > 0

        btnCheckout.isEnabled = isAnyItemSelected

        if (isAnyItemSelected) {
            btnCheckout.text = "Checkout (${selectedCount} Item)"
        } else {
            btnCheckout.text = "Pilih Item"
        }
    }
}