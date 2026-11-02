package com.example.azb_sataandagi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.azb_sataandagi.DetailActivity
import com.example.azb_sataandagi.Project.HomePageActivity
import com.example.azb_sataandagi.WebActivity
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

        binding.btnToProject.setOnClickListener {
            startActivity(Intent(ctx, HomePageActivity::class.java))
        }

        binding.btnWeb.setOnClickListener {
            startActivity(Intent(ctx, WebActivity::class.java))
        }

        binding.btnDetails.setOnClickListener {
            startActivity(Intent(ctx, DetailActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
