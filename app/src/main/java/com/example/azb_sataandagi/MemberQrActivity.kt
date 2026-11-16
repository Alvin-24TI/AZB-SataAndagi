package com.example.azb_sataandagi

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityMemberQrBinding
import com.example.azb_sataandagi.utils.SessionManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlin.math.abs

class MemberQrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemberQrBinding
    private lateinit var sessionManager: SessionManager

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_TIER = "extra_tier"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_EMAIL = "extra_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMemberQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Kartu & QR Code Member"
            subtitle = "Cafe Sobremesa Digital Membership"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupMemberData()

        binding.btnVouchers.setOnClickListener {
            startActivity(Intent(this, VoucherActivity::class.java))
        }

        binding.btnMemberList.setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }
    }

    private fun setupMemberData() {
        val nameExtra = intent.getStringExtra(EXTRA_NAME)
        val tierExtra = intent.getStringExtra(EXTRA_TIER)
        val phoneExtra = intent.getStringExtra(EXTRA_PHONE)
        val emailExtra = intent.getStringExtra(EXTRA_EMAIL)

        val username = sessionManager.username.ifEmpty { "Alvin Z. B." }
        val name = nameExtra ?: username
        val tier = tierExtra ?: sessionManager.memberTier
        val phone = phoneExtra ?: "081234567890"
        val email = emailExtra ?: "${username.lowercase().replace(" ", "")}@gmail.com"
        val memberCode = "SBM-${abs(name.hashCode() % 900000) + 100000}"

        binding.tvMemberName.text = name
        binding.tvMemberTier.text = tier
        binding.tvMemberCode.text = "ID Member: $memberCode"
        binding.tvMemberContact.text = "📞 $phone | ✉️ $email"

        // Generate QR Code data JSON payload
        val qrPayload = """
            {
              "app": "Cafe Sobremesa",
              "memberCode": "$memberCode",
              "name": "$name",
              "tier": "$tier",
              "phone": "$phone",
              "email": "$email"
            }
        """.trimIndent()

        val qrBitmap = generateQRCode(qrPayload)
        binding.imgQrCode.setImageBitmap(qrBitmap)
    }

    private fun generateQRCode(text: String, width: Int = 600, height: Int = 600): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
}
