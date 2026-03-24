package com.example.tripplanner.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.example.tripplanner.R
import com.example.tripplanner.data.local.database.AppDatabase
import com.example.tripplanner.ui.auth.AuthActivity
import com.example.tripplanner.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var database: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()


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

        CoroutineScope(Dispatchers.IO).launch {

            val userId = SessionManager.getUserId(requireContext())
            val user = database.userDao().getUserById(userId)

            requireActivity().runOnUiThread {

                if(user != null){
                    tvUserEmail.text = user.email
                }

            }
        }

        btnLogout.setOnClickListener {

            SessionManager.logout(requireContext())

            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()

        }

        val btnDeleteAccount = view.findViewById<Button>(R.id.btnDeleteAcount)

        btnDeleteAccount.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar cuenta")
                .setMessage("¿Seguro que quieres eliminar tu cuenta?")
                .setPositiveButton("Eliminar") { _, _ ->

                    CoroutineScope(Dispatchers.IO).launch {

                        val userId = SessionManager.getUserId(requireContext())
                        val user = database.userDao().getUserById(userId)

                        if(user != null){
                            database.userDao().deleteUser(user)
                        }

                        SessionManager.logout(requireContext())

                        requireActivity().runOnUiThread {

                            startActivity(Intent(requireContext(), AuthActivity::class.java))
                            requireActivity().finish()

                        }

                    }

                }
                .setNegativeButton("Cancelar", null)
                .show()

        }
    }
}