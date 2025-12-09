package com.example.bucqetbunga.adapters

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
import com.example.bucqetbunga.utils.CartManager

class CartAdapter(
    private val context: android.content.Context,
    private var cartItems: MutableList<CartItem>,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartManager = CartManager(context)

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivCartImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvCartName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvCartPrice)
        val quantityTextView: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val checkBox: CheckBox = itemView.findViewById(R.id.cbSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val bouquet = cartItem.bouquet

        // 1. SET DATA
        holder.imageView.setImageResource(bouquet.imageResId)
        holder.nameTextView.text = bouquet.name
        holder.priceTextView.text = bouquet.getFormattedPrice()
        holder.quantityTextView.text = cartItem.quantity.toString()
        holder.checkBox.isChecked = cartItem.isSelected

        // 2. CHECKBOX (SELECT/UNSELECT)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            cartItem.isSelected = isChecked
            cartManager.updateCartItem(cartItem)
            onDataChanged()
        }

        // 3. TOMBOL PLUS
        holder.btnPlus.setOnClickListener {
            cartItem.quantity++
            cartManager.updateCartItem(cartItem)
            holder.quantityTextView.text = cartItem.quantity.toString()
            onDataChanged()
        }

        // 4. TOMBOL MINUS
        holder.btnMinus.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                cartManager.updateCartItem(cartItem)
                holder.quantityTextView.text = cartItem.quantity.toString()
                onDataChanged()
            } else {
                // Hapus item jika quantity = 0
                cartManager.removeFromCart(cartItem.id)
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                onDataChanged()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateList(newList: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newList)
        notifyDataSetChanged()
    }
}