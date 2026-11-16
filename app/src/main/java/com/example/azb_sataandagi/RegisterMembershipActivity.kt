package com.example.azb_sataandagi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.azb_sataandagi.databinding.ActivityRegisterMembershipBinding
import com.example.azb_sataandagi.model.Member
import com.example.azb_sataandagi.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterMembershipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterMembershipBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterMembershipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_register_membership)
            subtitle = "Sistem Membership Cafe Sobremesa"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSubmitRegistration.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, getString(R.string.register_empty_error), Toast.LENGTH_SHORT).show()
            } else {
                val selectedTier = when (binding.rgMemberTier.checkedRadioButtonId) {
                    R.id.rbSilver -> "Silver Member"
                    R.id.rbPlatinum -> "Platinum Member"
                    else -> "Gold VIP"
                }

                val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val newMember = Member(
                    fullName = fullName,
                    phone = phone,
                    email = email,
                    tier = selectedTier,
                    registerDate = currentDate
                )

                sessionManager.addMember(newMember)
                sessionManager.isMemberRegistered = true
                sessionManager.memberTier = selectedTier

                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
