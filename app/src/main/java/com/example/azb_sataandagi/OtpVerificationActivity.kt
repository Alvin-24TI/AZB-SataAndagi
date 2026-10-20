package com.example.azb_sataandagi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityOtpVerificationBinding
import com.example.azb_sataandagi.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        val nama = intent.getStringExtra("EXTRA_NAMA") ?: ""
        val phone = intent.getStringExtra("EXTRA_PHONE") ?: ""
        val username = intent.getStringExtra("EXTRA_USERNAME") ?: ""
        val password = intent.getStringExtra("EXTRA_PASSWORD") ?: ""

        if (phone.isNotEmpty()) {
            binding.tvOtpInstruction.text = "Kode OTP dikirimkan ke nomor HP:\n$phone"
        }

        binding.btnVerifyOtp.setOnClickListener {
            val otpInput = binding.etOtp.text.toString().trim()

            if (otpInput.isEmpty()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.otp_dialog_title))
                    .setMessage(getString(R.string.otp_empty_error))
                    .setPositiveButton("OK", null)
                    .show()
            } else if (otpInput != phone) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.otp_dialog_title))
                    .setMessage(getString(R.string.otp_invalid_error))
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                // Save data to SharedPreferences only when OTP verification succeeds
                sessionManager.registerUser(nama, phone, username, password)

                Toast.makeText(this, getString(R.string.otp_success), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("REGISTERED_USERNAME", username)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
