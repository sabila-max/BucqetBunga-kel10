package com.example.bucqetbunga.adapters

import android.content.Context
import android.util.Log
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

    companion object {
        private const val TAG = "OrderAdapter"
    }

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
        try {
            val order = orders[position]

            // Set data dengan safe null handling
            holder.tvOrderId.text = "Order #${order.id.takeLast(8)}"
            holder.tvOrderDate.text = order.getFormattedDate()
            holder.tvCustomerName.text = order.customerName.ifEmpty { "Tidak ada nama" }
            holder.tvItemsSummary.text = order.getItemsSummary()
            holder.tvTotal.text = order.getFormattedTotal()
            holder.tvStatus.text = order.status

            // Set warna status
            val statusColor = when (order.status) {
                "Menunggu Pembayaran" -> android.R.color.holo_orange_dark
                "Diproses" -> android.R.color.holo_blue_dark
                "Dikirim" -> android.R.color.holo_purple
                "Selesai" -> android.R.color.holo_green_dark
                "Dibatalkan" -> android.R.color.holo_red_dark
                else -> android.R.color.darker_gray
            }

            holder.tvStatus.setTextColor(context.getColor(statusColor))

            Log.d(TAG, "Order bound: ${order.id}, items: ${order.items.size}")
        } catch (e: Exception) {
            Log.e(TAG, "Error binding order at position $position: ${e.message}", e)
            // Set default values untuk mencegah crash
            holder.tvOrderId.text = "Error loading order"
            holder.tvOrderDate.text = "-"
            holder.tvCustomerName.text = "-"
            holder.tvItemsSummary.text = "-"
            holder.tvTotal.text = "Rp 0"
            holder.tvStatus.text = "Error"
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateList(newList: List<Order>) {
        try {
            orders = newList
            notifyDataSetChanged()
            Log.d(TAG, "Order list updated: ${newList.size} orders")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating order list: ${e.message}", e)
        }
    }
}