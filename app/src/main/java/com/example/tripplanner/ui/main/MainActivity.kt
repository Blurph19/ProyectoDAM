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

            val userId = SessionManager.getUserId(this@MainActivity)

            val existingTrips = database.tripDao().getTripsByUser(userId)

            if (existingTrips.isEmpty()) {

                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val calendar = Calendar.getInstance()

                // 1️⃣ FINDE EN DUBLIN → PRÓXIMO (en 30 días)

                val startDublin = Calendar.getInstance()
                startDublin.add(Calendar.DAY_OF_YEAR, 30)

                val endDublin = Calendar.getInstance()
                endDublin.add(Calendar.DAY_OF_YEAR, 33)

                val trip1 = Trip(
                    userId = userId,
                    title = "Finde en Dublin",
                    destination = "Irlanda",
                    startDate = formatter.format(startDublin.time),
                    endDate = formatter.format(endDublin.time),
                    notes = "Primer viaje corto para conocer la ciudad"
                )

                // 2️⃣ ESCAPADA A ROMA → EN CURSO

                val startRome = Calendar.getInstance()
                startRome.add(Calendar.DAY_OF_YEAR, -1)

                val endRome = Calendar.getInstance()
                endRome.add(Calendar.DAY_OF_YEAR, 3)

                val trip2 = Trip(
                    userId = userId,
                    title = "Escapada a Roma",
                    destination = "Italia",
                    startDate = formatter.format(startRome.time),
                    endDate = formatter.format(endRome.time),
                    notes = "Visitar el Coliseo y el Vaticano"
                )

                // 3️⃣ VIAJE A PARÍS → FINALIZADO

                val startParis = Calendar.getInstance()
                startParis.add(Calendar.DAY_OF_YEAR, -40)

                val endParis = Calendar.getInstance()
                endParis.add(Calendar.DAY_OF_YEAR, -35)

                val trip3 = Trip(
                    userId = userId,
                    title = "Viaje a París",
                    destination = "Francia",
                    startDate = formatter.format(startParis.time),
                    endDate = formatter.format(endParis.time),
                    notes = "Museos y paseo por el Sena"
                )

                // 4️⃣ ROADTRIP DESDE REIKIAVIK → PENDIENTE (sin fechas)

                val trip4 = Trip(
                    userId = userId,
                    title = "Roadtrip desde Reikiavik",
                    destination = "Islandia",
                    startDate = null,
                    endDate = null,
                    notes = "Planificar ruta por Islandia"
                )

                val id1 = database.tripDao().insertTrip(trip1)
                val id2 = database.tripDao().insertTrip(trip2)
                val id3 = database.tripDao().insertTrip(trip3)
                val id4 = database.tripDao().insertTrip(trip4)

                // CHECKLIST DEMO

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id1.toInt(), name = "Pasaporte", isChecked = true)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id1.toInt(), name = "Maleta pequeña", isChecked = false)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id2.toInt(), name = "Entradas Coliseo", isChecked = true)
                )

                database.checklistDao().insertItem(
                    ChecklistItem(tripId = id2.toInt(), name = "Reservar hotel", isChecked = false)
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
            }
        }
    }
}