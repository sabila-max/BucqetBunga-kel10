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

    // Deklarasi komponen UI
    private lateinit var rvCart: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var emptyState: LinearLayout

    // CartManager untuk mengelola isi keranjang
    private lateinit var cartManager: CartManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_cart.xml
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // Inisialisasi CartManager dengan Context fragment
        cartManager = CartManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi komponen UI
        initViews(view)

        // Setup RecyclerView dan adapter
        setupRecyclerView()

        // Update tampilan saat fragment pertama kali dibuat
        updateUI()
    }

    override fun onResume() {
        super.onResume()

        // Update UI ketika kembali ke fragment
        updateUI()

        // Update badge cart di BottomNavigation (jumlah item terpilih)
        (activity as? MainActivity)?.updateCartBadge()
    }

    private fun initViews(view: View) {
        // Menghubungkan ID layout ke variabel
        rvCart = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        emptyState = view.findViewById(R.id.emptyState)
    }

    private fun setupRecyclerView() {

        // Adapter menerima list item dari CartManager
        adapter = CartAdapter(
            requireContext(),
            cartManager.getCartItems().toMutableList()
        ) {
            // Callback ketika jumlah/centang item berubah
            updateTotal()
            updateUI()

            // Update badge di MainActivity
            (activity as? MainActivity)?.updateCartBadge()
        }

        // Menampilkan list secara vertikal
        rvCart.layoutManager = LinearLayoutManager(context)
        rvCart.adapter = adapter

        // Tombol Checkout
        btnCheckout.setOnClickListener {
            val selectedItems = cartManager.getSelectedItems()

            // Cek apakah minimal satu item sudah dicentang
            if (selectedItems.isNotEmpty()) {
                val intent = Intent(activity, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "Pilih minimal satu item untuk Checkout!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateUI() {
        val cartItems = cartManager.getCartItems()

        if (cartItems.isEmpty()) {
            // Jika keranjang kosong → tampilkan empty state
            emptyState.visibility = View.VISIBLE
            rvCart.visibility = View.GONE

            // Total ditampilkan tetap Rp0
            tvTotal.text = cartManager.getFormattedTotal()

            // Nonaktifkan tombol checkout
            btnCheckout.isEnabled = false
            btnCheckout.text = "Checkout"
        } else {
            // Jika ada item → tampilkan RecyclerView
            emptyState.visibility = View.GONE
            rvCart.visibility = View.VISIBLE

            // Perbarui adapter dengan data terbaru
            adapter.updateList(cartItems)

            // Hitung ulang total harga
            updateTotal()
        }
    }

    private fun updateTotal() {
        // Tampilkan total harga format "Rp..."
        tvTotal.text = cartManager.getFormattedTotal()

        // Hitung jumlah item yang dicentang
        val selectedCount = cartManager.getSelectedItems().size
        val isAnyItemSelected = selectedCount > 0

        // Aktif/Nonaktifkan tombol checkout
        btnCheckout.isEnabled = isAnyItemSelected

        // Ganti text tombol sesuai jumlah item yang dipilih
        if (isAnyItemSelected) {
            btnCheckout.text = "Checkout (${selectedCount} Item)"
        } else {
            btnCheckout.text = "Pilih Item"
        }
    }
}
