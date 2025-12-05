package com.example.bucqetbunga.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.activities.CheckoutActivity
import com.example.bucqetbunga.adapters.CartAdapter
import com.example.bucqetbunga.utils.CartManager
import kotlin.jvm.java

class CartFragment : Fragment() {

    private lateinit var rvCart: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var emptyState: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

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
        rvCart = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        emptyState = view.findViewById(R.id.emptyState)
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(requireContext(), CartManager.getCartItems()) {
            updateTotal()
        }

        rvCart.layoutManager = LinearLayoutManager(context)
        rvCart.adapter = adapter

        btnCheckout.setOnClickListener {
            if (CartManager.getSelectedItems().isNotEmpty()) {
                val intent = Intent(activity, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUI() {
        val cartItems = CartManager.getCartItems()

        if (cartItems.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            rvCart.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            rvCart.visibility = View.VISIBLE
            adapter.updateList(cartItems)
        }

        updateTotal()
    }

    private fun updateTotal() {
        tvTotal.text = CartManager.getFormattedTotal()
    }
}