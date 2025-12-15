package com.example.bucqetbunga.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.adapters.OrderAdapter
import com.example.bucqetbunga.utils.OrderManager

class OrderFragment : Fragment() {

    private var rvOrders: RecyclerView? = null
    private var adapter: OrderAdapter? = null
    private var emptyState: LinearLayout? = null
    private var orderManager: OrderManager? = null

    companion object {
        private const val TAG = "OrderFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView started")

        return try {
            val view = inflater.inflate(R.layout.fragment_order, container, false)
            Log.d(TAG, "Layout inflated successfully")

            view
        } catch (e: Exception) {
            Log.e(TAG, "CRITICAL ERROR in onCreateView: ${e.message}", e)
            e.printStackTrace()

            // Buat view emergency sederhana
            createEmergencyView(inflater, container)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated started")

        try {
            // Step 1: Initialize OrderManager
            Log.d(TAG, "Initializing OrderManager...")
            orderManager = OrderManager(requireContext())
            Log.d(TAG, "OrderManager initialized successfully")

            // Step 2: Initialize Views
            Log.d(TAG, "Initializing views...")
            if (!initViews(view)) {
                Log.e(TAG, "Failed to initialize views")
                showErrorMessage(view, "Gagal menginisialisasi tampilan")
                return
            }
            Log.d(TAG, "Views initialized successfully")

            // Step 3: Setup RecyclerView
            Log.d(TAG, "Setting up RecyclerView...")
            if (!setupRecyclerView()) {
                Log.e(TAG, "Failed to setup RecyclerView")
                showErrorMessage(view, "Gagal mengatur RecyclerView")
                return
            }
            Log.d(TAG, "RecyclerView setup successfully")

            // Step 4: Load data
            Log.d(TAG, "Loading data...")
            updateUI()
            Log.d(TAG, "Data loaded successfully")

        } catch (e: Exception) {
            Log.e(TAG, "CRITICAL ERROR in onViewCreated: ${e.message}", e)
            e.printStackTrace()
            showErrorMessage(view, "Terjadi kesalahan: ${e.message}")
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

    private fun initViews(view: View): Boolean {
        return try {
            // Cari RecyclerView
            rvOrders = view.findViewById(R.id.rvOrders)
            if (rvOrders == null) {
                Log.e(TAG, "RecyclerView (rvOrders) not found in layout!")
                return false
            }
            Log.d(TAG, "RecyclerView found")

            // Cari Empty State
            emptyState = view.findViewById(R.id.emptyState)
            if (emptyState == null) {
                Log.e(TAG, "Empty state (emptyState) not found in layout!")
                return false
            }
            Log.d(TAG, "Empty state found")

            true
        } catch (e: Exception) {
            Log.e(TAG, "Exception in initViews: ${e.message}", e)
            false
        }
    }

    private fun setupRecyclerView(): Boolean {
        return try {
            val recycler = rvOrders ?: return false

            // Buat adapter dengan list kosong dulu
            adapter = OrderAdapter(requireContext(), emptyList())

            // Set LayoutManager
            recycler.layoutManager = LinearLayoutManager(requireContext())

            // Set Adapter
            recycler.adapter = adapter

            Log.d(TAG, "RecyclerView configured successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Exception in setupRecyclerView: ${e.message}", e)
            false
        }
    }

    private fun updateUI() {
        try {
            val manager = orderManager
            if (manager == null) {
                Log.e(TAG, "OrderManager is null in updateUI")
                showEmptyState()
                return
            }

            Log.d(TAG, "Getting orders from OrderManager...")
            val orders = manager.getOrders()
            Log.d(TAG, "Retrieved ${orders.size} orders")

            if (orders.isEmpty()) {
                Log.d(TAG, "No orders found, showing empty state")
                showEmptyState()
            } else {
                Log.d(TAG, "Showing ${orders.size} orders")
                showOrderList(orders.size)

                // Update adapter
                adapter?.updateList(orders) ?: Log.e(TAG, "Adapter is null!")
            }
        } catch (e: Exception) {
            Log.e(TAG, "CRITICAL ERROR in updateUI: ${e.message}", e)
            e.printStackTrace()
            showEmptyState()
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showEmptyState() {
        emptyState?.visibility = View.VISIBLE
        rvOrders?.visibility = View.GONE
        Log.d(TAG, "Empty state shown")
    }

    private fun showOrderList(count: Int) {
        emptyState?.visibility = View.GONE
        rvOrders?.visibility = View.VISIBLE
        Log.d(TAG, "Order list shown with $count items")
    }

    private fun showErrorMessage(view: View, message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

        // Coba tampilkan pesan error di TextView jika ada
        try {
            val errorText = view.findViewById<TextView>(R.id.tvEmptyMessage)
            errorText?.text = "Error: $message"
        } catch (e: Exception) {
            Log.e(TAG, "Could not show error in TextView: ${e.message}")
        }
    }

    private fun createEmergencyView(inflater: LayoutInflater, container: ViewGroup?): View {
        // Buat view sederhana jika layout gagal di-load
        val emergencyView = TextView(requireContext()).apply {
            text = "Error: Tidak dapat memuat halaman pesanan.\nSilakan restart aplikasi atau hubungi developer."
            textSize = 16f
            setPadding(32, 32, 32, 32)
        }
        return emergencyView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")

        // Clear references
        rvOrders = null
        adapter = null
        emptyState = null
        orderManager = null
    }
}