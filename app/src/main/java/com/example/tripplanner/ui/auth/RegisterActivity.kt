package com.example.tripplanner.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.tripplanner.R
import com.example.tripplanner.data.local.database.AppDatabase
import com.example.tripplanner.data.local.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val user = User(
                email = email.text.toString(),
                password = password.text.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.userDao().insertUser(user)

                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        }
    }
}