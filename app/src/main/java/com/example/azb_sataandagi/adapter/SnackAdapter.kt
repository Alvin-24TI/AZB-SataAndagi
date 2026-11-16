package com.example.azb_sataandagi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.azb_sataandagi.databinding.ItemSnackBinding
import com.example.azb_sataandagi.model.SnackItem

class SnackAdapter(
    private val snackList: List<SnackItem>,
    private val onItemClick: (SnackItem) -> Unit
) : RecyclerView.Adapter<SnackAdapter.SnackViewHolder>() {

    class SnackViewHolder(val binding: ItemSnackBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder {
        val binding =
            ItemSnackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SnackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
        val snack = snackList[position]
        with(holder.binding) {
            tvSnackName.text = snack.name
            tvSnackCategory.text = "Kategori: ${snack.category}"
            tvSnackPrice.text = snack.price
            imgSnack.setImageResource(snack.imageRes)

            root.setOnClickListener {
                onItemClick(snack)
            }
        }
    }

    override fun getItemCount(): Int = snackList.size
}
