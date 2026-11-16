package com.example.azb_sataandagi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.azb_sataandagi.databinding.ItemMenuBinding
import com.example.azb_sataandagi.model.MenuItem

class MenuAdapter(
    private val menuList: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding =
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        with(holder.binding) {
            tvMenuName.text = item.name
            tvMenuPrice.text = item.price
            tvMenuCategory.text = "Kategori: ${item.category}"
            tvMenuDesc.text = item.description
            imgMenu.setImageResource(item.imageRes)

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = menuList.size
}
