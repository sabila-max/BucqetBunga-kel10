package com.example.bucqetbunga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.adapters.OrderAdapter
import com.example.bucqetbunga.utils.CartManager

class OrderFragment : Fragment() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var adapter: OrderAdapter
    private lateinit var emptyState: LinearLayout
    // 1. Tambah CartManager
    private lateinit var cartManager: CartManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        // 2. Inisialisasi
        cartManager = CartManager(requireContext())

        initViews(view)
        setupRecyclerView()
        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun initViews(view: View) {
        rvOrders = view.findViewById(R.id.rvOrders)
        emptyState = view.findViewById(R.id.emptyState)
    }

    private fun setupRecyclerView() {
        // 3. Panggil getOrders dari instance cartManager
        adapter = OrderAdapter(requireContext(), cartManager.getOrders())
        rvOrders.layoutManager = LinearLayoutManager(context)
        rvOrders.adapter = adapter
    }

    private fun updateUI() {
        // 4. Panggil getOrders dari instance cartManager
        val orders = cartManager.getOrders()

        if (orders.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            rvOrders.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            rvOrders.visibility = View.VISIBLE
            adapter.updateList(orders)
        }
    }
}