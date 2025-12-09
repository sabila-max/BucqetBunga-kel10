package com.example.bucqetbunga

import android.content.Intent // FIX: Import Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.activities.DetailActivity // FIX: Import DetailActivity
import com.example.bucqetbunga.models.Bouquet

interface OnBouquetClickListener {
    fun onOrderClick(bouquet: Bouquet)
}

class BouquetAdapter(
    private val bouquets: MutableList<Bouquet>,
    private val listener: OnBouquetClickListener
) : RecyclerView.Adapter<BouquetAdapter.BouquetViewHolder>() {

    inner class BouquetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivBouquetImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvBouquetName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvBouquetDescription)
        val priceTextView: TextView = itemView.findViewById(R.id.tvBouquetPrice)
        val orderButton: Button = itemView.findViewById(R.id.btnOrder)
        val addToCartButton: ImageButton = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BouquetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bouquet, parent, false)
        return BouquetViewHolder(view)
    }

    fun updateList(newList: List<Bouquet>) {
        bouquets.clear()
        bouquets.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BouquetViewHolder, position: Int) {
        val bouquet = bouquets[position]

        // 1. Set Data Tampilan
        holder.imageView.setImageResource(bouquet.imageResId)
        holder.nameTextView.text = bouquet.name
        holder.descriptionTextView.text = "${bouquet.description}\nKategori: ${bouquet.category.name.replace("_", " ")}"
        holder.priceTextView.text = bouquet.getFormattedPrice()

        // 2. Navigasi ke Detail Activity (Klik pada gambar/card)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("BOUQUET_ID", bouquet.id) // Kirim ID produk
            holder.itemView.context.startActivity(intent)
        }

        // 3. Listener Tombol Pesan & Keranjang (Quick Add)
        val clickListener = View.OnClickListener {
            listener.onOrderClick(bouquet)
        }

        // Pastikan tombol selalu aktif (Sesuai request "Selalu Tersedia")
        holder.orderButton.isEnabled = true
        holder.addToCartButton.isEnabled = true

        holder.orderButton.setOnClickListener(clickListener)
        holder.addToCartButton.setOnClickListener(clickListener)
    }

    override fun getItemCount(): Int = bouquets.size
}