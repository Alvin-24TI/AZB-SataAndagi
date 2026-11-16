package com.example.azb_sataandagi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.azb_sataandagi.adapter.MemberListAdapter
import com.example.azb_sataandagi.databinding.ActivityMemberListBinding
import com.example.azb_sataandagi.utils.SessionManager

class MemberListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemberListBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMemberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Daftar Member"
            subtitle = "Cafe Sobremesa Membership System"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnRegisterNewMember.setOnClickListener {
            startActivity(Intent(this, RegisterMembershipActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadMemberList()
    }

    private fun loadMemberList() {
        val memberList = sessionManager.getMemberList()
        binding.tvTotalMembers.text = "Total: ${memberList.size} Member Terdaftar"

        if (memberList.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvMemberList.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvMemberList.visibility = View.VISIBLE
            binding.rvMemberList.layoutManager = LinearLayoutManager(this)
            binding.rvMemberList.adapter = MemberListAdapter(memberList)
        }
    }
}
