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
import com.example.bucqetbunga.BouquetAdapter
import com.example.bucqetbunga.OnBouquetClickListener
import com.example.bucqetbunga.data.BouquetDataSource
import com.example.bucqetbunga.models.Bouquet

class DashboardFragment : Fragment(), OnBouquetClickListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false) //
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvBouquets)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val bouquetList = BouquetDataSource.getBouquets()

        val adapter = BouquetAdapter(bouquetList, this)
        recyclerView.adapter = adapter
    }

    override fun onOrderClick(bouquet: Bouquet) {

        Toast.makeText(context, "${bouquet.name} (Rp${bouquet.price}) ditambahkan ke Keranjang!", Toast.LENGTH_SHORT).show()
    }
}