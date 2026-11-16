package com.example.azb_sataandagi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.azb_sataandagi.MemberQrActivity
import com.example.azb_sataandagi.databinding.ItemMemberBinding
import com.example.azb_sataandagi.model.Member

class MemberListAdapter(private val memberList: List<Member>) :
    RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {

    class MemberViewHolder(val binding: ItemMemberBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding =
            ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = memberList[position]
        val context = holder.itemView.context
        with(holder.binding) {
            tvMemberName.text = member.fullName
            tvMemberTier.text = member.tier
            tvMemberPhone.text = "📞 ${member.phone}"
            tvMemberEmail.text = "✉️ ${member.email}"
            tvMemberDate.text = "Terdaftar: ${member.registerDate}"

            root.setOnClickListener {
                val intent = Intent(context, MemberQrActivity::class.java).apply {
                    putExtra(MemberQrActivity.EXTRA_NAME, member.fullName)
                    putExtra(MemberQrActivity.EXTRA_TIER, member.tier)
                    putExtra(MemberQrActivity.EXTRA_PHONE, member.phone)
                    putExtra(MemberQrActivity.EXTRA_EMAIL, member.email)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = memberList.size
}
