package com.example.azb_sataandagi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityLoginBinding
import com.example.azb_sataandagi.utils.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        val regUserFromIntent = intent.getStringExtra("REGISTERED_USERNAME")
        if (!regUserFromIntent.isNullOrEmpty()) {
            binding.etUsername.setText(regUserFromIntent)
        } else if (sessionManager.registeredUsername.isNotEmpty()) {
            binding.etUsername.setText(sessionManager.registeredUsername)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val regUser = sessionManager.registeredUsername
            val regPass = sessionManager.registeredPassword

            val isValidLogin = if (regUser.isNotEmpty()) {
                (username == regUser && password == regPass) || (username == password)
            } else {
                username == password
            }

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_empty_error), Toast.LENGTH_SHORT).show()
            } else if (isValidLogin) {
                sessionManager.isLogin = true
                sessionManager.username = username

                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Gagal! Username atau password salah.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}
