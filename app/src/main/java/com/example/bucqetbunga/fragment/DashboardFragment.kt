package com.example.bucqetbunga.fragments

import android.content.res.Resources // <-- IMPORT BARU
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.BouquetAdapter
import com.example.bucqetbunga.OnBouquetClickListener
import com.example.bucqetbunga.data.BouquetDataSource
import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.utils.CartManager // <-- Import CartManager (object)
import com.example.bucqetbunga.BouquetCategory // Import Enum Category
import com.example.bucqetbunga.activities.MainActivity // <-- Import MainActivity untuk badge

class DashboardFragment : Fragment(), OnBouquetClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BouquetAdapter
    private lateinit var categoryContainer: LinearLayout

    // Simpan semua data buket yang tidak difilter
    private val allBouquets = BouquetDataSource.getBouquets()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // HAPUS: private lateinit var cartManager: CartManager
        // HAPUS: cartManager = CartManager(requireContext()) karena CartManager adalah OBJECT (singleton)

        // Pastikan ID 'categoryContainer' ada di fragment_dashboard.xml
        categoryContainer = view.findViewById(R.id.categoryContainer)

        setupRecyclerView()
        createCategoryButtons()

        // Tampilkan semua produk secara default saat pertama kali dimuat
        filterBouquets(null)
    }

    private fun setupRecyclerView() {
        recyclerView = view?.findViewById(R.id.rvBouquets)!!
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inisialisasi adapter dengan daftar lengkap data (gunakan .toMutableList() untuk kompatibilitas updateList)
        adapter = BouquetAdapter(allBouquets.toMutableList(), this)
        recyclerView.adapter = adapter
    }

    // --- FUNGSI UTILITY HILANG (dpToPx) DITAMBAHKAN DI SINI ---
    // Menyediakan fungsi konversi dp ke px, mengatasi error 'unresolved reference: dpToPx'
    private fun Int.dpToPx(resources: Resources): Int =
        (this * resources.displayMetrics.density + 0.5f).toInt()

    // --- FUNGSI PEMBUATAN TOMBOL KATEGORI (MENGATASI UNRESOLVED REFERENCES) ---
    private fun createCategoryButtons() {
        categoryContainer.removeAllViews()

        // 1. Tambahkan tombol "Semua"
        addCategoryButton("Semua", null)

        // 2. Tambahkan tombol untuk setiap kategori
        BouquetCategory.values().forEach { category ->
            addCategoryButton(category.name.replace("_", " "), category)
        }
    }

    private fun addCategoryButton(name: String, category: BouquetCategory?) {
        val button = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                40.dpToPx(resources)
            ).apply {
                setMargins(4.dpToPx(resources), 0, 4.dpToPx(resources), 0)
            }
            text = name
            textSize = 12f
            setTag(category)

            // Atur gaya awal
            val isSelected = category == null
            // Pastikan Anda telah membuat resource category_button_selected/default
            setBackgroundResource(if (isSelected) R.drawable.category_button_selected else R.drawable.category_button_default)
            // Asumsi: R.color.dark_text_color ada, jika tidak, ganti dengan Color.BLACK
            setTextColor(if (isSelected) Color.WHITE else resources.getColor(R.color.dark_text_color, null))

            setOnClickListener {
                handleCategoryClick(this)
            }
        }
        categoryContainer.addView(button)
    }

    private fun handleCategoryClick(clickedButton: Button) {
        val selectedCategory = clickedButton.tag as BouquetCategory?

        // Reset gaya semua tombol
        for (i in 0 until categoryContainer.childCount) {
            val child = categoryContainer.getChildAt(i) as Button
            child.setBackgroundResource(R.drawable.category_button_default)
            child.setTextColor(resources.getColor(R.color.dark_text_color, null))
        }

        // Atur gaya tombol yang diklik
        clickedButton.setBackgroundResource(R.drawable.category_button_selected)
        clickedButton.setTextColor(Color.WHITE)

        // Terapkan filter
        filterBouquets(selectedCategory)
    }

    private fun filterBouquets(category: BouquetCategory?) {
        val filteredList = if (category == null) {
            allBouquets
        } else {
            allBouquets.filter { it.category == category }
        }

        adapter.updateList(filteredList)
    }

    // Implementasi dari OnBouquetClickListener
    override fun onOrderClick(bouquet: Bouquet) {
        // PANGGIL CARTMANAGER LANGSUNG SEBAGAI OBJECT
        CartManager.addToCart(bouquet) // <-- KOREKSI PENGGUNAAN OBJECT MANAGER

        Toast.makeText(context, "${bouquet.name} berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()

        // Panggil fungsi update Cart Badge di MainActivity
        (activity as? MainActivity)?.updateCartBadge()
    }
}