package com.example.bucqetbunga.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.models.Bouquet

interface OnBouquetClickListener {
    fun onOrderClick(bouquet: Bouquet)
}

class BouquetAdapter(
    private var bouquets: List<Bouquet>,
    private val listener: OnBouquetClickListener
) : RecyclerView.Adapter<BouquetAdapter.BouquetViewHolder>() {

    inner class BouquetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivBouquetImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvBouquetName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvBouquetDescription)
        val priceTextView: TextView = itemView.findViewById(R.id.tvBouquetPrice)
        val orderButton: Button = itemView.findViewById(R.id.btnOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BouquetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bouquet, parent, false)
        return BouquetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BouquetViewHolder, position: Int) {
        val bouquet = bouquets[position]

        holder.imageView.setImageResource(bouquet.imageResId)
        holder.nameTextView.text = bouquet.name
        holder.descriptionTextView.text = bouquet.description
        holder.priceTextView.text = bouquet.getFormattedPrice()

        val stockLabel = if (bouquet.stock > 0) "Tersedia: ${bouquet.stock}" else "Habis"
        holder.descriptionTextView.append("\nKategori: ${bouquet.category.name} | $stockLabel")

        holder.orderButton.isEnabled = bouquet.stock > 0
        holder.orderButton.setOnClickListener {
            listener.onOrderClick(bouquet)
        }
    }

    override fun getItemCount(): Int = bouquets.size

    // FIX: Tambahkan method updateList yang diperlukan oleh DashboardFragment
    fun updateList(newList: List<Bouquet>) {
        bouquets = newList
        notifyDataSetChanged()
    }
}