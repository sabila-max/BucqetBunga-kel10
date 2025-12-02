package com.example.bucqetbunga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.models.Bouquet // Import data class yang baru

// Interface untuk mengirim event klik ke Fragment (reusable)
interface OnBouquetClickListener {
    fun onOrderClick(bouquet: Bouquet)
}

class BouquetAdapter(
    private val bouquets: List<Bouquet>,
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
            .inflate(R.layout.item_bouquet, parent, false) // Menggunakan item_bouquet.xml
        return BouquetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BouquetViewHolder, position: Int) {
        val bouquet = bouquets[position]

        // Menghubungkan data ke View
        holder.imageView.setImageResource(bouquet.imageResId) // Menggunakan Resource ID (Int)
        holder.nameTextView.text = bouquet.name
        holder.descriptionTextView.text = bouquet.description

        // Menggunakan fungsi format harga dari Model (RAPI dan REUSABLE!)
        holder.priceTextView.text = bouquet.getFormattedPrice()

        // Menambahkan label stok (contoh)
        val stockLabel = if (bouquet.stock > 0) "Tersedia: ${bouquet.stock}" else "Habis"
        holder.descriptionTextView.append("\nKategori: ${bouquet.category.name} | $stockLabel")

        // Handle Tombol Pesan
        holder.orderButton.isEnabled = bouquet.stock > 0 // Tombol non-aktif jika stok habis
        holder.orderButton.setOnClickListener {
            listener.onOrderClick(bouquet) // Mengirim objek Bouquet ke Fragment
        }
    }

    override fun getItemCount(): Int = bouquets.size
}