package com.example.bucqetbunga.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.models.Order

class OrderAdapter(
    private val context: Context,
    private var orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvOrderDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvCustomerName: TextView = view.findViewById(R.id.tvCustomerName)
        val tvItemsSummary: TextView = view.findViewById(R.id.tvItemsSummary)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val cardView: CardView = view.findViewById(R.id.cardOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        holder.tvOrderId.text = "Order #${order.id}"
        holder.tvOrderDate.text = order.getFormattedDate()
        holder.tvCustomerName.text = order.customerName
        holder.tvItemsSummary.text = order.getItemsSummary()
        holder.tvTotal.text = order.getFormattedTotal()
        holder.tvStatus.text = order.status

        // Set warna status
        when (order.status) {
            "Menunggu Pembayaran" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_orange_dark))
            "Diproses" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_blue_dark))
            "Dikirim" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_purple))
            "Selesai" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_green_dark))
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateList(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }
}