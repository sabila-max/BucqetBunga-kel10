package com.example.bucqetbunga.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.bucqetbunga.R
import com.example.bucqetbunga.activities.LoginActivity
import com.example.bucqetbunga.utils.SessionManager

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil tombol dari layout
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // 1. Besihkan session
            val session = SessionManager(requireContext())
            session.logout()

            // 2. Kembali ke LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            // Flag: Clear semua activity, buat stack baru
            intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}