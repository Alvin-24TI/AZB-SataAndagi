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
import com.example.azb_sataandagi.adapter.MenuAdapter
import com.example.azb_sataandagi.api.RetrofitClient
import com.example.azb_sataandagi.databinding.ActivityMenuBinding
import com.example.azb_sataandagi.model.MenuItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var menuAdapter: MenuAdapter
    private var menuList: MutableList<MenuItem> = mutableListOf()

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
            subtitle = "Sobremesa Sata Andagi & Beverage (Supabase CRUD)"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()

        binding.fabAddMenu.setOnClickListener {
            showAddMenuDialog()
        }

        fetchMenuFromSupabase()
    }

    private fun setupRecyclerView() {
        menuAdapter = MenuAdapter(
            menuList = menuList,
            onItemClick = { item ->
                Toast.makeText(this, "Menu: ${item.name} (${item.price})", Toast.LENGTH_SHORT).show()
            },
            onEditClick = { item ->
                showEditMenuDialog(item)
            },
            onDeleteClick = { item ->
                confirmDeleteMenu(item)
            }
        )
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = menuAdapter
    }

    private fun fetchMenuFromSupabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getMenuItems()
                if (response.isSuccessful && response.body() != null) {
                    val fetchedList = response.body()!!
                    withContext(Dispatchers.Main) {
                        menuList.clear()
                        if (fetchedList.isNotEmpty()) {
                            menuList.addAll(fetchedList)
                        } else {
                            seedDefaultMenuToSupabase()
                        }
                        menuAdapter.updateList(menuList)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Code: ${response.code()}"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MenuActivity, "Gagal Supabase (${response.code()}): $errorMsg", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MenuActivity, "Error koneksi: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun seedDefaultMenuToSupabase() {
        val defaultItems = listOf(
            MenuItem(name = "Sata Andagi Original", price = "Rp 15.000", category = "Snack Okinawa", description = "Donat khas Okinawa yang renyah di luar dan lembut di dalam."),
            MenuItem(name = "Sata Andagi Coklat Keju", price = "Rp 18.000", category = "Snack Variant", description = "Perpaduan lelehan coklat manis dan parutan keju gurih."),
            MenuItem(name = "Sata Andagi Matcha Special", price = "Rp 20.000", category = "Snack Variant", description = "Topping bubuk Matcha asli khas Jepang yang harum."),
            MenuItem(name = "Kopi Susu Sobremesa", price = "Rp 22.000", category = "Beverage Specialty", description = "Kopi espresso pilihan dicampur dengan susu creamy spesial.")
        )
        lifecycleScope.launch(Dispatchers.IO) {
            for (item in defaultItems) {
                try {
                    RetrofitClient.instance.insertMenuItem(item)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val response = RetrofitClient.instance.getMenuItems()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    menuList.clear()
                    menuList.addAll(response.body()!!)
                    menuAdapter.updateList(menuList)
                }
            }
        }
    }

    private fun showAddMenuDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_menu, null)
        val etName = dialogView.findViewById<EditText>(R.id.etDialogMenuName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etDialogMenuPrice)
        val etCategory = dialogView.findViewById<EditText>(R.id.etDialogMenuCategory)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDialogMenuDesc)

        MaterialAlertDialogBuilder(this)
            .setTitle("Tambah Menu Baru (Supabase)")
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val name = etName.text.toString().trim()
                val price = etPrice.text.toString().trim()
                val category = etCategory.text.toString().trim()
                val desc = etDesc.text.toString().trim()

                if (name.isNotEmpty() && price.isNotEmpty()) {
                    val newItem = MenuItem(name = name, price = price, category = if (category.isEmpty()) "Snack" else category, description = desc)
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val res = RetrofitClient.instance.insertMenuItem(newItem)
                            if (res.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@MenuActivity, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                    fetchMenuFromSupabase()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Toast.makeText(this, "Nama dan Harga tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditMenuDialog(item: MenuItem) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_menu, null)
        val etName = dialogView.findViewById<EditText>(R.id.etDialogMenuName)
        val etPrice = dialogView.findViewById<EditText>(R.id.etDialogMenuPrice)
        val etCategory = dialogView.findViewById<EditText>(R.id.etDialogMenuCategory)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDialogMenuDesc)

        etName.setText(item.name)
        etPrice.setText(item.price)
        etCategory.setText(item.category)
        etDesc.setText(item.description)

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Menu (Supabase)")
            .setView(dialogView)
            .setPositiveButton("Update") { dialog, _ ->
                val name = etName.text.toString().trim()
                val price = etPrice.text.toString().trim()
                val category = etCategory.text.toString().trim()
                val desc = etDesc.text.toString().trim()

                if (name.isNotEmpty() && price.isNotEmpty() && item.id != null) {
                    val updatedItem = MenuItem(id = item.id, name = name, price = price, category = category, description = desc)
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val res = RetrofitClient.instance.updateMenuItem("eq.${item.id}", updatedItem)
                            if (res.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@MenuActivity, "Menu berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                    fetchMenuFromSupabase()
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

    private fun confirmDeleteMenu(item: MenuItem) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Hapus Menu")
            .setMessage("Apakah Anda yakin ingin menghapus '${item.name}' dari Supabase?")
            .setPositiveButton("Hapus") { dialog, _ ->
                if (item.id != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val res = RetrofitClient.instance.deleteMenuItem("eq.${item.id}")
                            if (res.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@MenuActivity, "Menu berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                    fetchMenuFromSupabase()
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
}
