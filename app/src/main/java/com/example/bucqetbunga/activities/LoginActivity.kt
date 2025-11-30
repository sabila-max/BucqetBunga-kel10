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

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnGetStarted: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        // Cek apakah sudah login
        if (sessionManager.isLoggedIn()) {
            navigateToMain()
            return
        }

        // Tampilkan welcome screen
        setContentView(R.layout.activity_login)

        btnGetStarted = findViewById(R.id.btnGetStarted)
        btnGetStarted.setOnClickListener {
            showLoginForm()
        }
    }

    private fun showLoginForm() {
        // Ganti ke layout login form
        setContentView(R.layout.activity_login_form)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validasi input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // Login sederhana (untuk demo)
        if (email == "salsabilaabel63@gmail.com" && password == "123456") {
            sessionManager.createLoginSession(email, "User")
            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}