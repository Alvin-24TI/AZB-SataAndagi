package com.example.azb_sataandagi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.azb_sataandagi.adapter.MenuAdapter
import com.example.azb_sataandagi.databinding.ActivityMenuBinding
import com.example.azb_sataandagi.model.MenuItem

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Katalog Menu Cafe"
            subtitle = "Sobremesa Sata Andagi & Beverage"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupMenuRecyclerView()
    }

    private fun setupMenuRecyclerView() {
        val menuList = listOf(
            MenuItem(
                "Sata Andagi Original",
                "Rp 15.000",
                "Snack Okinawa",
                "Donat khas Okinawa yang renyah di luar dan lembut di dalam.",
                R.drawable.satandagi
            ),
            MenuItem(
                "Sata Andagi Coklat Keju",
                "Rp 18.000",
                "Snack Variant",
                "Perpaduan lelehan coklat manis dan parutan keju gurih.",
                R.drawable.sataandagi
            ),
            MenuItem(
                "Sata Andagi Matcha Special",
                "Rp 20.000",
                "Snack Variant",
                "Topping bubuk Matcha asli khas Jepang yang harum.",
                R.drawable.logo
            ),
            MenuItem(
                "Kopi Susu Sobremesa",
                "Rp 22.000",
                "Beverage Specialty",
                "Kopi espresso pilihan dicampur dengan susu creamy spesial.",
                R.drawable.logo
            ),
            MenuItem(
                "Okinawa Milk Tea",
                "Rp 24.000",
                "Beverage Specialty",
                "Teh susu khas Okinawa dengan sirup gula aren autentik.",
                R.drawable.satandagi
            )
        )

        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = MenuAdapter(menuList) { item ->
            Toast.makeText(this, "Menu dipilih: ${item.name} (${item.price})", Toast.LENGTH_SHORT).show()
        }
    }
}
