package com.example.bucqetbunga.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bucqetbunga.R
import com.example.bucqetbunga.utils.SessionManager // Pastikan SessionManager ada di utils

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi UI components
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmailRegister)
        etPassword = findViewById(R.id.etPasswordRegister)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginLink = findViewById(R.id.tvLoginLink)

        // Inisialisasi Session Manager (Meskipun tidak akan digunakan untuk menyimpan multiple user, ini untuk konsistensi)
        sessionManager = SessionManager(this)

        // Handle Tombol Daftar
        btnRegister.setOnClickListener {
            performRegistration()
        }

        // Handle Link Kembali ke Login
        tvLoginLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun performRegistration() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // 1. Validasi Input Kosong
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_LONG).show()
            return
        }

        // 2. Validasi Format Email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_LONG).show()
            return
        }

        // 3. Validasi Kecocokan Password
        if (password != confirmPassword) {
            Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_LONG).show()
            return
        }

        // 4. (Opsional) Validasi Panjang Password
        if (password.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(this, "Pendaftaran berhasil! Silakan masuk dengan akun Anda.", Toast.LENGTH_LONG).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        // Hapus activity di atasnya (RegisterActivity) dari stack
        startActivity(intent)
        finish()
    }
}