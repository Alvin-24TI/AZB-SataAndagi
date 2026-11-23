package com.example.azb_sataandagi

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.azb_sataandagi.adapter.VoucherAdapter
import com.example.azb_sataandagi.api.RetrofitClient
import com.example.azb_sataandagi.databinding.ActivityVoucherBinding
import com.example.azb_sataandagi.model.Voucher
import com.example.azb_sataandagi.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoucherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVoucherBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var voucherAdapter: VoucherAdapter
    private var voucherList: MutableList<Voucher> = mutableListOf()

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
            subtitle = "Sistem Poin Member Cafe Sobremesa (Supabase CRUD)"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()

        binding.fabAddVoucher.setOnClickListener {
            showAddVoucherDialog()
        }

        fetchVouchersFromSupabase()
    }

    private fun setupRecyclerView() {
        voucherAdapter = VoucherAdapter(
            voucherList = voucherList,
            onRedeemClick = { voucher ->
                redeemVoucher(voucher)
            },
            onEditClick = { voucher ->
                showEditVoucherDialog(voucher)
            },
            onDeleteClick = { voucher ->
                confirmDeleteVoucher(voucher)
            }
        )
        binding.rvVouchers.layoutManager = LinearLayoutManager(this)
        binding.rvVouchers.adapter = voucherAdapter
    }

    private fun updatePointsUI() {
        val points = sessionManager.memberPoints
        val tier = sessionManager.memberTier
        binding.tvUserPoints.text = "🪙 $points Poin"
        binding.tvMemberTier.text = tier
    }

    private fun fetchVouchersFromSupabase() {
        updatePointsUI()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getVouchers()
                if (response.isSuccessful && response.body() != null) {
                    val fetchedList = response.body()!!
                    withContext(Dispatchers.Main) {
                        voucherList.clear()
                        if (fetchedList.isNotEmpty()) {
                            voucherList.addAll(fetchedList)
                        } else {
                            seedDefaultVouchersToSupabase()
                        }
                        voucherAdapter.updateList(voucherList)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Code: ${response.code()}"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@VoucherActivity, "Gagal Supabase (${response.code()}): $errorMsg", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@VoucherActivity, "Error koneksi: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun seedDefaultVouchersToSupabase() {
        val defaultVouchers = listOf(
            Voucher(title = "Diskon 10% All Menu", description = "Potongan 10% untuk seluruh varian Sata Andagi & Minuman Cafe Sobremesa.", pointCost = 100, voucherCode = "SOBREMESA10"),
            Voucher(title = "Gratis 1 Beverage Cafe", description = "Voucher penukaran 1 cangkir Kopi/Teh Spesial Sobremesa.", pointCost = 200, voucherCode = "FREEBEVERAGE"),
            Voucher(title = "Voucher Potongan Rp 25.000", description = "Potongan harga Rp 25.000 dengan minimal transaksi Rp 50.000.", pointCost = 300, voucherCode = "DISCOUNT25K"),
            Voucher(title = "Buy 1 Get 1 Sata Andagi", description = "Beli 1 Porsi Sata Andagi gratis 1 Porsi Sata Andagi Original.", pointCost = 400, voucherCode = "B1G1ANDAGI")
        )
        lifecycleScope.launch(Dispatchers.IO) {
            for (voucher in defaultVouchers) {
                try {
                    RetrofitClient.instance.insertVoucher(voucher)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val response = RetrofitClient.instance.getVouchers()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    voucherList.clear()
                    voucherList.addAll(response.body()!!)
                    voucherAdapter.updateList(voucherList)
                }
            }
        }
    }

    private fun showAddVoucherDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_voucher, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etDialogVoucherTitle)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDialogVoucherDesc)
        val etPoint = dialogView.findViewById<EditText>(R.id.etDialogVoucherPoint)
        val etCode = dialogView.findViewById<EditText>(R.id.etDialogVoucherCode)

        MaterialAlertDialogBuilder(this)
            .setTitle("Tambah Voucher Baru (Supabase)")
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val title = etTitle.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val pointStr = etPoint.text.toString().trim()
                val code = etCode.text.toString().trim()

                if (title.isNotEmpty() && code.isNotEmpty()) {
                    val pointCost = pointStr.toIntOrNull() ?: 100
                    val newVoucher = Voucher(title = title, description = desc, pointCost = pointCost, voucherCode = code)
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val res = RetrofitClient.instance.insertVoucher(newVoucher)
                            if (res.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@VoucherActivity, "Voucher berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                    fetchVouchersFromSupabase()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Toast.makeText(this, "Judul dan Kode Voucher wajib diisi!", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditVoucherDialog(voucher: Voucher) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_voucher, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etDialogVoucherTitle)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDialogVoucherDesc)
        val etPoint = dialogView.findViewById<EditText>(R.id.etDialogVoucherPoint)
        val etCode = dialogView.findViewById<EditText>(R.id.etDialogVoucherCode)

        etTitle.setText(voucher.title)
        etDesc.setText(voucher.description)
        etPoint.setText(voucher.pointCost.toString())
        etCode.setText(voucher.voucherCode)

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Voucher (Supabase)")
            .setView(dialogView)
            .setPositiveButton("Update") { dialog, _ ->
                val title = etTitle.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val pointStr = etPoint.text.toString().trim()
                val code = etCode.text.toString().trim()

                if (title.isNotEmpty() && code.isNotEmpty() && voucher.id != null) {
                    val pointCost = pointStr.toIntOrNull() ?: voucher.pointCost
                    val updatedVoucher = Voucher(id = voucher.id, title = title, description = desc, pointCost = pointCost, voucherCode = code, isClaimed = voucher.isClaimed)
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val res = RetrofitClient.instance.updateVoucher("eq.${voucher.id}", updatedVoucher)
                            if (res.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@VoucherActivity, "Voucher berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                    fetchVouchersFromSupabase()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun confirmDeleteVoucher(voucher: Voucher) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus Voucher")
            .setMessage("Apakah Anda yakin ingin menghapus '${voucher.title}' dari Supabase?")
            .setPositiveButton("Hapus") { dialog, _ ->
                if (voucher.id != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val res = RetrofitClient.instance.deleteVoucher("eq.${voucher.id}")
                            if (res.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@VoucherActivity, "Voucher berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                    fetchVouchersFromSupabase()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun redeemVoucher(voucher: Voucher) {
        val currentPoints = sessionManager.memberPoints
        if (currentPoints >= voucher.pointCost) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi Penukaran Poin")
                .setMessage("Tukarkan ${voucher.pointCost} poin untuk voucher \"${voucher.title}\"?")
                .setPositiveButton("Tukarkan") { dialog, _ ->
                    sessionManager.memberPoints = currentPoints - voucher.pointCost
                    
                    // Update isClaimed in Supabase
                    if (voucher.id != null) {
                        voucher.isClaimed = true
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                RetrofitClient.instance.updateVoucher("eq.${voucher.id}", voucher)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    MaterialAlertDialogBuilder(this)
                        .setTitle("🎉 Penukaran Berhasil!")
                        .setMessage("Kode Voucher Anda: ${voucher.voucherCode}\n\nTunjukkan kode voucher ini kepada kasir Cafe Sobremesa saat bertransaksi!")
                        .setPositiveButton("OK") { innerDialog, _ ->
                            innerDialog.dismiss()
                            fetchVouchersFromSupabase()
                        }
                        .show()

                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
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
