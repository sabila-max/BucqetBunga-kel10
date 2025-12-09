package com.example.bucqetbunga.fragments

import android.os.Bundle
import android.util.Log
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
    private lateinit var cartManager: CartManager

    companion object {
        private const val TAG = "OrderFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return try {
            val view = inflater.inflate(R.layout.fragment_order, container, false)
            cartManager = CartManager(requireContext())
            view
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreateView: ${e.message}", e)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        try {
            initViews(view)
            setupRecyclerView()
            updateUI()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated: ${e.message}", e)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
        try {
            updateUI()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onResume: ${e.message}", e)
        }
    }

    private fun initViews(view: View) {
        try {
            rvOrders = view.findViewById(R.id.rvOrders)
            emptyState = view.findViewById(R.id.emptyState)
            Log.d(TAG, "Views initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views: ${e.message}", e)
            throw e
        }
    }

    private fun setupRecyclerView() {
        try {
            adapter = OrderAdapter(requireContext(), emptyList())
            rvOrders.layoutManager = LinearLayoutManager(context)
            rvOrders.adapter = adapter
            Log.d(TAG, "RecyclerView setup successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView: ${e.message}", e)
            throw e
        }
    }

    private fun updateUI() {
        try {
            val orders = cartManager.getOrders()
            Log.d(TAG, "Number of orders: ${orders.size}")

            if (orders.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                rvOrders.visibility = View.GONE
                Log.d(TAG, "Showing empty state")
            } else {
                emptyState.visibility = View.GONE
                rvOrders.visibility = View.VISIBLE
                adapter.updateList(orders)
                Log.d(TAG, "Showing ${orders.size} orders")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating UI: ${e.message}", e)
        }
    }
}