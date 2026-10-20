package com.example.azb_sataandagi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.azb_sataandagi.DeveloperProfileActivity
import com.example.azb_sataandagi.LoginActivity
import com.example.azb_sataandagi.R
import com.example.azb_sataandagi.databinding.FragmentProfileBinding
import com.example.azb_sataandagi.utils.SessionManager

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ctx = requireContext()
        val sessionManager = SessionManager(ctx)
        val name = sessionManager.fullName.ifEmpty { sessionManager.username.ifEmpty { "User" } }
        val username = sessionManager.username.ifEmpty { "User" }
        val phone = sessionManager.phone.ifEmpty { "-" }

        binding.tvProfileName.text = "Nama: $name"
        binding.tvProfileEmail.text = "Username: $username | No HP: $phone"

        binding.btnDevProfile.setOnClickListener {
            startActivity(Intent(ctx, DeveloperProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            Toast.makeText(ctx, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

            val intent = Intent(ctx, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
