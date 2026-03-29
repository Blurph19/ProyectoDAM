package com.example.tripplanner.ui.auth

import android.R.attr.password
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.tripplanner.R
import com.example.tripplanner.ui.main.MainActivity
import com.example.tripplanner.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.tripplanner.data.local.database.AppDatabase
import com.example.tripplanner.data.local.entity.User
import com.google.android.material.textfield.TextInputEditText

class AuthActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val userId = SessionManager.getUserId(this)

        if (userId != -1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_auth)
        enableEdgeToEdge()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        lifecycleScope.launch(Dispatchers.IO) {

            val existingUser = database.userDao().getUserByEmail("demo123")

            if (existingUser == null) {
                val demoUser = User(
                    email = "demo123",
                    password = "demo123"
                )
                database.userDao().insertUser(demoUser)
            }
        }

        val etUser = findViewById<TextInputEditText>(R.id.etUser)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Botón Registro
        findViewById<Button>(R.id.btnRegister).setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Login

        btnEntrar.setOnClickListener {

            val email = etUser.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce email y contraseña", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {

                val user = database.userDao().login(email, password)

                runOnUiThread {

                    if(user != null){

                        // Guardar sesión
                        SessionManager.saveUserId(this@AuthActivity, user.id)

                        // Abrir app
                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(this@AuthActivity,"Credenciales incorrectas",Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            }
        }

    }
}