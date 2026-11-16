package com.example.azb_sataandagi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.azb_sataandagi.MemberListActivity
import com.example.azb_sataandagi.MemberQrActivity
import com.example.azb_sataandagi.QrScannerActivity
import com.example.azb_sataandagi.RegisterMembershipActivity
import com.example.azb_sataandagi.VoucherActivity
import com.example.azb_sataandagi.databinding.FragmentHomeBinding
import com.example.azb_sataandagi.utils.SessionManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx = requireContext()
        val sessionManager = SessionManager(ctx)
        val username = sessionManager.username
        if (username.isNotEmpty()) {
            binding.tvWelcomeUser.text = "Selamat Datang, $username!"
        }

        binding.btnRegisterMembership.setOnClickListener {
            startActivity(Intent(ctx, RegisterMembershipActivity::class.java))
        }

        binding.btnMemberQr.setOnClickListener {
            startActivity(Intent(ctx, MemberQrActivity::class.java))
        }

        binding.btnScanQr.setOnClickListener {
            startActivity(Intent(ctx, QrScannerActivity::class.java))
        }

        binding.btnMemberList.setOnClickListener {
            startActivity(Intent(ctx, MemberListActivity::class.java))
        }

        binding.btnVouchers.setOnClickListener {
            startActivity(Intent(ctx, VoucherActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
