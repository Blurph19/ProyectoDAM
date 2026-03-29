package com.example.tripplanner.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.R
import com.example.tripplanner.ui.tripdetail.TripDetailFragment
import com.example.tripplanner.ui.trips.TripListFragment
import com.example.tripplanner.data.local.entity.Trip
import androidx.room.Room
import com.example.tripplanner.data.local.database.AppDatabase
import com.example.tripplanner.data.local.entity.ChecklistItem
import com.example.tripplanner.data.local.entity.Expense
import com.example.tripplanner.data.local.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tripplanner.ui.countdown.CountdownFragment
import com.example.tripplanner.ui.profile.ProfileFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.tripplanner.utils.SessionManager

class MainActivity : AppCompatActivity(),
    TripListFragment.OnTripSelectedListener {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val userId = SessionManager.getUserId(this)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.BottomNavigation)

        val appBar = findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.appBarHome)

        bottomNavigation.setOnItemSelectedListener { item ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

            when (item.itemId) {
                R.id.nav_trips -> {

                    appBar.visibility = View.VISIBLE

                    if (currentFragment !is TripListFragment) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, TripListFragment())
                            .commit()
                    }
                    true
                }

                R.id.nav_countdown -> {

                    appBar.visibility = View.GONE

                    if (currentFragment !is CountdownFragment) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, CountdownFragment())
                            .commit()
                    }
                    true
                }

                R.id.nav_profile -> {

                    appBar.visibility = View.GONE

                    if (currentFragment !is ProfileFragment) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, ProfileFragment())
                            .commit()
                    }
                    true
                }

                else -> false
            }
        }

        bottomNavigation.selectedItemId = R.id.nav_trips
        appBar.visibility = View.VISIBLE


        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        insertSampleTrips()
        loadTrips()

    }

    override fun onResume() {
        super.onResume()
        loadTrips()
    }

    override fun onTripSelected(trip: Trip) {

        val detailFragment = TripDetailFragment.newInstance(trip)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()

    }

    fun loadTrips() {
        CoroutineScope(Dispatchers.IO).launch {

            val userId = SessionManager.getUserId(this@MainActivity)

            val trips = database.tripDao().getTripsByUser(userId)

            runOnUiThread {

                val fragment = supportFragmentManager
                    .findFragmentById(R.id.fragmentContainer) as? TripListFragment

                fragment?.updateTrips(trips)
            }
        }
    }

    private fun insertSampleTrips() {

        CoroutineScope(Dispatchers.IO).launch {

            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val demoInserted = prefs.getBoolean("demo_inserted", false)

            val userId = SessionManager.getUserId(this@MainActivity)

            if (!demoInserted) {

                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance()


                // 1 - Viaje a Japón: PRÓXIMO (en 30 días)

                val startJapan = Calendar.getInstance()
                startJapan.add(Calendar.DAY_OF_YEAR, 30)

                val endJapan = Calendar.getInstance()
                endJapan.add(Calendar.DAY_OF_YEAR, 33)

                val trip1 = Trip(
                    userId = userId,
                    title = "Vacaciones en Japón",
                    destination = "Japón",
                    startDate = formatter.format(startJapan.time),
                    endDate = formatter.format(endJapan.time),
                    notes = "Viaje de 21 días para conocer Japón",
                    imageUri = "android.resource://${packageName}/${R.drawable.japan}"
                )

                // 2 - VERANO EN PEÑÍSCOLA: PRÓXIMO (en 30 días)

                val startSpain = Calendar.getInstance()
                startSpain.add(Calendar.DAY_OF_YEAR, -1)

                val endSpain = Calendar.getInstance()
                endSpain.add(Calendar.DAY_OF_YEAR, 3)

                val trip2 = Trip(
                    userId = userId,
                    title = "Verano en Peñíscola",
                    destination = "España",
                    startDate = formatter.format(startSpain.time),
                    endDate = formatter.format(endSpain.time),
                    notes = "Viaje corto por la ciudad",
                    imageUri = "android.resource://${packageName}/${R.drawable.peniscola}"

                )

                // 3️ - ESCAPADA A PARÍS: FINALIZADO

                val startParis = Calendar.getInstance()
                startParis.add(Calendar.DAY_OF_YEAR, -40)

                val endParis = Calendar.getInstance()
                endParis.add(Calendar.DAY_OF_YEAR, -35)

                val trip3 = Trip(
                    userId = userId,
                    title = "Escapada a París",
                    destination = "Francia",
                    startDate = formatter.format(startParis.time),
                    endDate = formatter.format(endParis.time),
                    notes = "Museos y paseo por el Sena",
                    imageUri = "android.resource://${packageName}/${R.drawable.paris}"

                )

                // 4 - FINDE EN DUBLIN: PENDIENTE (sin fechas)

                val trip4 = Trip(
                    userId = userId,
                    title = "Finde en Dublín",
                    destination = "Irlanda",
                    startDate = null,
                    endDate = null,
                    notes = "Planificar ruta por Irlanda",
                    imageUri = "android.resource://${packageName}/${R.drawable.dublin}"
                )

                val id1 = database.tripDao().insertTrip(trip1)
                val id2 = database.tripDao().insertTrip(trip2)
                val id3 = database.tripDao().insertTrip(trip3)
                val id4 = database.tripDao().insertTrip(trip4)

                // CHECKLIST DEMO

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id1.toInt(), name = "Reservar Hotel", isChecked = true)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id1.toInt(), name = "Preparar la maleta", isChecked = false)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id2.toInt(), name = "Entradas al castillo", isChecked = true)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id2.toInt(), name = "Reserva en el restaurante", isChecked = false)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id3.toInt(), name = "Entradas Louvre", isChecked = true)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id3.toInt(), name = "Paseo en barco por el Sena", isChecked = false)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id4.toInt(), name = "Buscar vuelos baratos", isChecked = false)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id4.toInt(), name = "Ruta por Irlanda", isChecked = false)
                )

                // PRESUPUESTO DEMO

                database.expenseDao().insertExpense(
                    Expense(tripId = id1.toInt(), title = "Vuelo", amount = 120.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id1.toInt(), title = "Hotel", amount = 200.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id2.toInt(), title = "Hotel", amount = 320.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id2.toInt(), title = "Comida", amount = 120.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id3.toInt(), title = "Museos", amount = 80.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id3.toInt(), title = "Comida", amount = 150.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id4.toInt(), title = "Vuelos estimados", amount = 200.0)
                )

                database.expenseDao().insertExpense(
                    Expense(tripId = id4.toInt(), title = "Alojamiento", amount = 300.0)
                )

                prefs.edit().putBoolean("demo_inserted", true).apply()
            }
        }
    }
}