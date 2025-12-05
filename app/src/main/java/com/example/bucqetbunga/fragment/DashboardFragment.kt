// File: DashboardFragment.kt

package com.example.bucqetbunga.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bucqetbunga.R
import com.example.bucqetbunga.BouquetAdapter // Import Adapter
import com.example.bucqetbunga.OnBouquetClickListener // Import Listener
import com.example.bucqetbunga.data.BouquetDataSource // <-- Import Data Source yang sudah diperbaiki
import com.example.bucqetbunga.models.Bouquet // Import Model

// Fragment ini harus mengimplementasikan interface OnBouquetClickListener
class DashboardFragment : Fragment(), OnBouquetClickListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan layout fragment_dashboard.xml
        return inflater.inflate(R.layout.fragment_dashboard, container, false) //
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inisialisasi RecyclerView (Asumsi ID-nya rvBouquets di layout)
        recyclerView = view.findViewById(R.id.rvBouquets)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 2. AMBIL DATA DARI DATA SOURCE yang sudah dikoreksi
        val bouquetList = BouquetDataSource.getBouquets()

        // 3. Pasang Adapter
        val adapter = BouquetAdapter(bouquetList, this)
        recyclerView.adapter = adapter
    }

    // Implementasi dari OnBouquetClickListener (Logika saat tombol "Pesan Sekarang" ditekan)
    override fun onOrderClick(bouquet: Bouquet) {
        // Logika Keranjang (Offline Ordering) akan diletakkan di sini.
        Toast.makeText(context, "${bouquet.name} ditambahkan ke Keranjang!", Toast.LENGTH_SHORT).show()
    }
}