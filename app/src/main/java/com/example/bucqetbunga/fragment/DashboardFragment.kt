package com.example.bucqetbunga.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bucqetbunga.R

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gunakan layout sederhana dulu untuk testing
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return view
    }
}