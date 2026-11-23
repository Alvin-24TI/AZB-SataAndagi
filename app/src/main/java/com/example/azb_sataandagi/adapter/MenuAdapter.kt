package com.example.azb_sataandagi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.azb_sataandagi.databinding.ItemMenuBinding
import com.example.azb_sataandagi.model.MenuItem

class MenuAdapter(
    private var menuList: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit,
    private val onEditClick: (MenuItem) -> Unit,
    private val onDeleteClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding =
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    fun updateList(newList: List<MenuItem>) {
        menuList = newList
        notifyDataSetChanged()
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
            btnEditMenu.setOnClickListener {
                onEditClick(item)
            }
            btnDeleteMenu.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun getItemCount(): Int = menuList.size
}
