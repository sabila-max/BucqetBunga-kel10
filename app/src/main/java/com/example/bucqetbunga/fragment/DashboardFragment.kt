package com.example.bucqetbunga.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat // FIX: Import ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.BouquetAdapter
import com.example.bucqetbunga.OnBouquetClickListener
import com.example.bucqetbunga.data.BouquetDataSource
import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.utils.CartManager
import com.example.bucqetbunga.BouquetCategory
import com.example.bucqetbunga.activities.MainActivity

class DashboardFragment : Fragment(), OnBouquetClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BouquetAdapter
    private lateinit var categoryContainer: LinearLayout
    private lateinit var cartManager: CartManager

    private val allBouquets = BouquetDataSource.getBouquets()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi CartManager
        cartManager = CartManager(requireContext())

        categoryContainer = view.findViewById(R.id.categoryContainer)

        setupRecyclerView()
        createCategoryButtons()

        // Default filter: Semua (null)
        filterBouquets(null)
    }

    private fun setupRecyclerView() {
        recyclerView = view?.findViewById(R.id.rvBouquets)!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BouquetAdapter(allBouquets.toMutableList(), this)
        recyclerView.adapter = adapter
    }

    private fun Int.dpToPx(resources: Resources): Int =
        (this * resources.displayMetrics.density + 0.5f).toInt()

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

            val isSelected = category == null // Default 'Semua' terpilih di awal
            updateButtonStyle(this, isSelected)

            setOnClickListener {
                handleCategoryClick(this)
            }
        }
        categoryContainer.addView(button)
    }

    private fun handleCategoryClick(clickedButton: Button) {
        val selectedCategory = clickedButton.tag as BouquetCategory?

        // Reset gaya SEMUA tombol di container
        for (i in 0 until categoryContainer.childCount) {
            val child = categoryContainer.getChildAt(i) as Button
            updateButtonStyle(child, false) // Set ke gaya default
        }

        // Set gaya tombol yang DIKLIK menjadi terpilih
        updateButtonStyle(clickedButton, true)

        filterBouquets(selectedCategory)
    }

    // Fungsi helper untuk mengubah gaya tombol
    private fun updateButtonStyle(button: Button, isSelected: Boolean) {
        if (isSelected) {
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_selected)
            button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        } else {
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_default)
            // FIX: Menggunakan Color.BLACK jika dark_text_color belum ada, atau pastikan colors.xml sudah benar
            // Opsional: Ganti android.R.color.black dengan R.color.dark_text_color jika sudah ada
            button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        }
    }

    private fun filterBouquets(category: BouquetCategory?) {
        val filteredList = if (category == null) {
            allBouquets
        } else {
            allBouquets.filter { it.category == category }
        }
        adapter.updateList(filteredList)
    }

    override fun onOrderClick(bouquet: Bouquet) {
        cartManager.addItemToCart(bouquet)
        Toast.makeText(context, "${bouquet.name} berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
        (activity as? MainActivity)?.updateCartBadge()
    }
}