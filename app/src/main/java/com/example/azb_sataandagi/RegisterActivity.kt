package com.example.azb_sataandagi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityRegisterBinding
import com.example.azb_sataandagi.utils.SessionManager

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        binding.btnRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (nama.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.register_empty_error), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, OtpVerificationActivity::class.java).apply {
                    putExtra("EXTRA_NAMA", nama)
                    putExtra("EXTRA_PHONE", phone)
                    putExtra("EXTRA_USERNAME", username)
                    putExtra("EXTRA_PASSWORD", password)
                }
                startActivity(intent)
            }
        }

        binding.btnGoToLogin.setOnClickListener {
            finish()
        }
    }
}
