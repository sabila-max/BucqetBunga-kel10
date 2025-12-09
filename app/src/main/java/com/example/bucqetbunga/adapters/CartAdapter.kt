package com.example.bucqetbunga.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.models.CartItem

class CartAdapter(
    private val context: Context,
    private var cartItems: List<CartItem>,
    private val onItemChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbSelect: CheckBox = view.findViewById(R.id.cbSelect)
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnDecrease: Button = view.findViewById(R.id.btnDecrease)
        val btnIncrease: Button = view.findViewById(R.id.btnIncrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]

        holder.tvProductName.text = cartItem.bouquet.name
        holder.tvProductPrice.text = cartItem.getFormattedTotal()
        holder.tvQuantity.text = cartItem.quantity.toString()
        holder.ivProduct.setImageResource(cartItem.bouquet.imageResId)

        // FIX: Set checkbox tanpa trigger listener dulu
        holder.cbSelect.setOnCheckedChangeListener(null)
        holder.cbSelect.isChecked = cartItem.isSelected
        holder.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            cartItem.isSelected = isChecked
            onItemChanged()
        }

        holder.btnDecrease.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                holder.tvQuantity.text = cartItem.quantity.toString()
                holder.tvProductPrice.text = cartItem.getFormattedTotal()
                onItemChanged()
            }
        }

        holder.btnIncrease.setOnClickListener {
            // FIX: Tambahkan validasi stok
            if (cartItem.quantity < cartItem.bouquet.stock) {
                cartItem.quantity++
                holder.tvQuantity.text = cartItem.quantity.toString()
                holder.tvProductPrice.text = cartItem.getFormattedTotal()
                onItemChanged()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateList(newList: List<CartItem>) {
        cartItems = newList
        notifyDataSetChanged()
    }
}