package com.example.azb_sataandagi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.azb_sataandagi.adapter.VoucherAdapter
import com.example.azb_sataandagi.databinding.ActivityVoucherBinding
import com.example.azb_sataandagi.model.Voucher
import com.example.azb_sataandagi.utils.SessionManager

class VoucherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoucherBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Poin & Voucher Diskon"
            subtitle = "Sistem Poin Member Cafe Sobremesa"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loadVoucherData()
    }

    private fun loadVoucherData() {
        val points = sessionManager.memberPoints
        val tier = sessionManager.memberTier
        binding.tvUserPoints.text = "🪙 $points Poin"
        binding.tvMemberTier.text = tier

        val claimedSet = sessionManager.getClaimedVoucherIds()

        val vouchers = listOf(
            Voucher(
                "v1",
                "Diskon 10% All Menu",
                "Potongan 10% untuk seluruh varian Sata Andagi & Minuman Cafe Sobremesa.",
                100,
                "SOBREMESA10",
                claimedSet.contains("v1")
            ),
            Voucher(
                "v2",
                "Gratis 1 Beverage Cafe",
                "Voucher penukaran 1 cangkir Kopi/Teh Spesial Sobremesa.",
                200,
                "FREEBEVERAGE",
                claimedSet.contains("v2")
            ),
            Voucher(
                "v3",
                "Voucher Potongan Rp 25.000",
                "Potongan harga Rp 25.000 dengan minimal transaksi Rp 50.000.",
                300,
                "DISCOUNT25K",
                claimedSet.contains("v3")
            ),
            Voucher(
                "v4",
                "Buy 1 Get 1 Sata Andagi",
                "Beli 1 Porsi Sata Andagi gratis 1 Porsi Sata Andagi Original.",
                400,
                "B1G1ANDAGI",
                claimedSet.contains("v4")
            )
        )

        binding.rvVouchers.layoutManager = LinearLayoutManager(this)
        binding.rvVouchers.adapter = VoucherAdapter(vouchers) { voucher ->
            redeemVoucher(voucher)
        }
    }

    private fun redeemVoucher(voucher: Voucher) {
        val currentPoints = sessionManager.memberPoints
        if (currentPoints >= voucher.pointCost) {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Penukaran Poin")
                .setMessage("Tukarkan ${voucher.pointCost} poin untuk voucher \"${voucher.title}\"?")
                .setPositiveButton("Tukarkan") { _, _ ->
                    sessionManager.memberPoints = currentPoints - voucher.pointCost
                    sessionManager.claimVoucher(voucher.id)

                    AlertDialog.Builder(this)
                        .setTitle("🎉 Penukaran Berhasil!")
                        .setMessage("Kode Voucher Anda: ${voucher.voucherCode}\n\nTunjukkan kode voucher ini kepada kasir Cafe Sobremesa saat bertransaksi!")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            loadVoucherData()
                        }
                        .show()
                }
                .setNegativeButton("Batal", null)
                .show()
        } else {
            Toast.makeText(
                this,
                "Poin Anda tidak cukup! Butuh ${voucher.pointCost} poin.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
