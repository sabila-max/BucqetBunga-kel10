package com.example.bucqetbunga.activities
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bucqetbunga.R
import com.example.bucqetbunga.utils.SessionManager
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    // Deklarasi view components
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button  // = variabel belum diisi sekarang, tapi nanti pasti diisi
    // Kalau akses sebelum diisi = error
    private lateinit var btnGetStarted: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        // ===== CEK SUDAH LOGIN ATAU BELUM =====
        if (sessionManager.isLoggedIn()) {
            navigateToMain()  // Langsung ke MainActivity
            return            // Stop eksekusi
        }

        // ===== TAMPILKAN WELCOME SCREEN =====
        setContentView(R.layout.activity_login)

        btnGetStarted = findViewById(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            showLoginForm()  // Ganti ke form login
        }
    }

    private fun showLoginForm() {
        // Ganti layout ke login form
        setContentView(R.layout.activity_login_form)

        // Binding view components
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            // Code yang jalan saat button diklik
            performLogin()
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // ===== VALIDASI INPUT =====

        // 1. Cek kosong atau tidak
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            return  // Stop function
        }

        // 2. Cek format email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // ===== LOGIN (HARDCODED DEMO) =====
        if (email == "salsabilaabel63@gmail.com" && password == "123456") {
            // Login berhasil
            sessionManager.createLoginSession(email, "User")
            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            // Login gagal
            Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)  // Buka activity baru
        finish()  // Tutup LoginActivity supaya gak bisa back
    }
}