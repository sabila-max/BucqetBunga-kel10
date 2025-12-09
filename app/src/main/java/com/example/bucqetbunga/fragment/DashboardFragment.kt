package com.example.bucqetbunga.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        // FIX: Mengembalikan type return ke View? agar kompatibel dengan Fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartManager = CartManager(requireContext())

        // FIX: Unresolved reference 'categoryContainer'
        categoryContainer = view.findViewById(R.id.categoryContainer)

        setupRecyclerView(view)
        setupCategoryButtons()

        filterBouquets(null)
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvBouquets)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BouquetAdapter(allBouquets.toMutableList(), this)
        recyclerView.adapter = adapter
    }

    // FIX: dpToPx harus menggunakan resources, dan tidak boleh dibulatkan terlalu cepat
    private fun Int.dpToPx(): Int =
        (this * resources.displayMetrics.density + 0.5f).toInt()

    private fun setupCategoryButtons() {
        categoryContainer.removeAllViews()

        addCategoryButton("Semua", null)

        // FIX: Menggunakan values() untuk kompatibilitas, walaupun entries() disarankan
        BouquetCategory.values().forEach { category ->
            addCategoryButton(category.name.replace("_", " "), category)
        }
    }

    private fun addCategoryButton(label: String, category: BouquetCategory?) {
        val btn = Button(requireContext()).apply {
            text = label
            textSize = 12f
            tag = category

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                45.dpToPx()
            ).apply {
                setMargins(6.dpToPx(), 0, 6.dpToPx(), 0)
            }

            // FIX: Resource Call
            background = ContextCompat.getDrawable(
                requireContext(),
                if (category == null)
                    R.drawable.category_button_selected
                else
                    R.drawable.category_button_default
            )

            // FIX: Resource Call
            setTextColor(
                if (category == null)
                    ContextCompat.getColor(requireContext(), android.R.color.white)
                else
                    ContextCompat.getColor(requireContext(), R.color.dark_text_color)
            )

            setOnClickListener { handleCategoryClick(this) }
        }

        categoryContainer.addView(btn)
    }

    private fun handleCategoryClick(button: Button) {
        val selectedCategory = button.tag as BouquetCategory?

        // reset semua button
        for (i in 0 until categoryContainer.childCount) {
            val child = categoryContainer.getChildAt(i) as Button
            updateButtonStyle(child, false)
        }

        // style untuk button yang dipilih
        updateButtonStyle(button, true)

        filterBouquets(selectedCategory)
    }

    // Helper untuk styling tombol
    private fun updateButtonStyle(button: Button, isSelected: Boolean) {
        if (isSelected) {
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_selected)
            button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        } else {
            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_default)
            // FIX: Menggunakan R.color.dark_text_color yang harus didefinisikan
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_text_color))
        }
    }

    private fun filterBouquets(category: BouquetCategory?) {
        val filtered = if (category == null)
            allBouquets
        else
            allBouquets.filter { it.category == category }

        adapter.updateList(filtered)
    }

    override fun onOrderClick(bouquet: Bouquet) {
        cartManager.addItemToCart(bouquet) // FIX: Panggil fungsi addItemToCart yang sudah diperbaiki
        Toast.makeText(requireContext(), "${bouquet.name} ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()

        (activity as? MainActivity)?.updateCartBadge()
    }
}