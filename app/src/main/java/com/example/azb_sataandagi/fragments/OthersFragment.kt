package com.example.azb_sataandagi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.azb_sataandagi.DetailActivity
import com.example.azb_sataandagi.DeveloperProfileActivity
import com.example.azb_sataandagi.Project.MembershipActivity
import com.example.azb_sataandagi.WebActivity
import com.example.azb_sataandagi.databinding.FragmentOthersBinding

class OthersFragment : Fragment() {
    private var _binding: FragmentOthersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOthersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx = requireContext()

        binding.btnDetails.setOnClickListener {
            startActivity(Intent(ctx, DetailActivity::class.java))
        }

        binding.btnMembership.setOnClickListener {
            startActivity(Intent(ctx, MembershipActivity::class.java))
        }

        binding.btnDeveloperProfile.setOnClickListener {
            startActivity(Intent(ctx, DeveloperProfileActivity::class.java))
        }

        binding.btnYouTube.setOnClickListener {
            startActivity(Intent(ctx, WebActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
