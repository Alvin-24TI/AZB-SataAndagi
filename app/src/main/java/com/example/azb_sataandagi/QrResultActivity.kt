package com.example.azb_sataandagi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityQrResultBinding
import org.json.JSONObject

class QrResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrResultBinding

    companion object {
        const val EXTRA_RAW_QR = "extra_raw_qr"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityQrResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Hasil Scan QR Code"
            subtitle = "Informasi Member Cafe Sobremesa"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        displayQrData()

        binding.btnVouchers.setOnClickListener {
            startActivity(Intent(this, VoucherActivity::class.java))
        }

        binding.btnScanAgain.setOnClickListener {
            startActivity(Intent(this, QrScannerActivity::class.java))
            finish()
        }
    }

    private fun displayQrData() {
        val rawContent = intent.getStringExtra(EXTRA_RAW_QR) ?: "Belum ada data QR"
        binding.tvRawPayload.text = rawContent

        var name = "Member Sobremesa"
        var code = "SBM-MEMBER"
        var tier = "Gold VIP Member"
        var phone = "-"
        var email = "-"

        try {
            val json = JSONObject(rawContent)
            name = json.optString("name", name)
            code = json.optString("memberCode", code)
            tier = json.optString("tier", tier)
            phone = json.optString("phone", phone)
            email = json.optString("email", email)
        } catch (e: Exception) {
            // Non-JSON QR code fallback parsing
            if (rawContent.contains("http", ignoreCase = true)) {
                name = "Link / URL QR Code"
                code = rawContent
            } else {
                name = rawContent
            }
        }

        binding.tvResultName.text = name
        binding.tvResultCode.text = code
        binding.tvResultTier.text = tier
        binding.tvResultContact.text = "📞 $phone | ✉️ $email"
    }
}
