package com.example.azb_sataandagi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.azb_sataandagi.api.RetrofitClient
import com.example.azb_sataandagi.databinding.ActivityLoginBinding
import com.example.azb_sataandagi.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_empty_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Authenticate with Supabase Users Table
            lifecycleScope.launch(Dispatchers.IO) {
                var loginSuccess = false
                var isSupabaseChecked = false

                try {
                    val response = RetrofitClient.instance.getUsers("eq.$username")
                    if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                        isSupabaseChecked = true
                        val user = response.body()!![0]
                        if (user.password == password) {
                            loginSuccess = true
                            sessionManager.registerUser(user.nama, user.phone, user.username, user.password)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Fallback to local session check if offline or not checked in Supabase
                if (!isSupabaseChecked) {
                    val regUser = sessionManager.registeredUsername
                    val regPass = sessionManager.registeredPassword
                    if (regUser.isNotEmpty()) {
                        loginSuccess = (username == regUser && password == regPass) || (username == password)
                    } else {
                        loginSuccess = (username == password)
                    }
                }

                withContext(Dispatchers.Main) {
                    if (loginSuccess) {
                        sessionManager.isLogin = true
                        sessionManager.username = username

                        Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Gagal! Username atau password salah.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
