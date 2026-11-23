package com.example.azb_sataandagi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.azb_sataandagi.databinding.ItemVoucherBinding
import com.example.azb_sataandagi.model.Voucher

class VoucherAdapter(
    private var voucherList: List<Voucher>,
    private val onRedeemClick: (Voucher) -> Unit,
    private val onEditClick: (Voucher) -> Unit,
    private val onDeleteClick: (Voucher) -> Unit
) : RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    class VoucherViewHolder(val binding: ItemVoucherBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val binding =
            ItemVoucherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoucherViewHolder(binding)
    }

    fun updateList(newList: List<Voucher>) {
        voucherList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        val voucher = voucherList[position]
        with(holder.binding) {
            tvVoucherTitle.text = voucher.title
            tvVoucherDescription.text = voucher.description
            tvPointCost.text = "🪙 ${voucher.pointCost} Poin"

            if (voucher.isClaimed) {
                tvVoucherCode.visibility = View.VISIBLE
                tvVoucherCode.text = "Kode Diskon: ${voucher.voucherCode}"
                btnRedeemVoucher.text = "Sudah Ditukarkan"
                btnRedeemVoucher.isEnabled = false
            } else {
                tvVoucherCode.visibility = View.GONE
                btnRedeemVoucher.text = "Tukarkan Poin"
                btnRedeemVoucher.isEnabled = true
                btnRedeemVoucher.setOnClickListener {
                    onRedeemClick(voucher)
                }
            }

            btnEditVoucher.setOnClickListener {
                onEditClick(voucher)
            }
            btnDeleteVoucher.setOnClickListener {
                onDeleteClick(voucher)
            }
        }
    }

    override fun getItemCount(): Int = voucherList.size
}
