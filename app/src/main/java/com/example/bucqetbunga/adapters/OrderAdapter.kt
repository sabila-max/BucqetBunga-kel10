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

    // ViewHolder menyimpan semua komponen UI dari item_order.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)        // ID pesanan
        val tvOrderDate: TextView = view.findViewById(R.id.tvOrderDate)    // Tanggal pesanan
        val tvCustomerName: TextView = view.findViewById(R.id.tvCustomerName) // Nama pelanggan
        val tvItemsSummary: TextView = view.findViewById(R.id.tvItemsSummary) // Ringkasan item
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)            // Total harga
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)          // Status pesanan
        val cardView: CardView = view.findViewById(R.id.cardOrder)         // Card item
    }

    // Membuat tampilan item dari layout item_order.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    // Menghubungkan data pesanan ke tampilan item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        // Mengisi teks ke setiap TextView
        holder.tvOrderId.text = "Order #${order.id}"
        holder.tvOrderDate.text = order.getFormattedDate()
        holder.tvCustomerName.text = order.customerName
        holder.tvItemsSummary.text = order.getItemsSummary()
        holder.tvTotal.text = order.getFormattedTotal()
        holder.tvStatus.text = order.status

        // Mengatur warna teks status berdasarkan nilai status
        when (order.status) {
            "Menunggu Pembayaran" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_orange_dark))
            "Diproses" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_blue_dark))
            "Dikirim" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_purple))
            "Selesai" -> holder.tvStatus.setTextColor(context.getColor(android.R.color.holo_green_dark))
        }
    }

    // Mengembalikan jumlah item dalam list
    override fun getItemCount(): Int = orders.size

    // Memperbarui list pesanan dan menyegarkan tampilan RecyclerView
    fun updateList(newList: List<Order>) {
        orders = newList
        notifyDataSetChanged()
    }
}
