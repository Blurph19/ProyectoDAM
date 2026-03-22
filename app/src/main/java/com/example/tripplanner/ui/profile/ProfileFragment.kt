package com.example.tripplanner.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.tripplanner.R
import com.example.tripplanner.ui.auth.AuthActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {

            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)

            requireActivity().finish()
        }

        val prefs = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)

        val email = prefs.getString("email", "usuario@demo.com")

        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)

        tvUserEmail.text = email
    }
}