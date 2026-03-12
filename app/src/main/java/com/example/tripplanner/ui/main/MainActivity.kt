package com.example.tripplanner.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.R
import com.example.tripplanner.ui.tripdetail.TripDetailFragment
import com.example.tripplanner.ui.trips.TripListFragment
import com.example.tripplanner.data.local.entity.Trip
import androidx.room.Room
import com.example.tripplanner.data.local.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.tripplanner.ui.countdown.CountdownFragment
import com.example.tripplanner.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity(),
    TripListFragment.OnTripSelectedListener {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.BottomNavigation)

        bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_trips -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TripListFragment())
                        .commit()

                    true
                }

                R.id.nav_countdown -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CountdownFragment())
                        .commit()

                    true
                }

                R.id.nav_profile -> {

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileFragment())
                        .commit()

                    true
                }

                else -> false
            }
        }

        bottomNavigation.selectedItemId = R.id.nav_trips

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        ).build()

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

            val trips = database.tripDao().getAllTrips()

            runOnUiThread {

                val fragment = supportFragmentManager
                    .findFragmentById(R.id.fragmentContainer) as? TripListFragment

                fragment?.updateTrips(trips)
            }
        }
    }

    private fun insertSampleTrips() {
        CoroutineScope(Dispatchers.IO).launch {

            val existingTrips = database.tripDao().getAllTrips()

            if (existingTrips.isEmpty()) {

                val trip1 = Trip(
                    title = "Viaje a Roma",
                    destination = "Roma",
                    startDate = null,
                    endDate = null,
                    notes = "Visitar el Coliseo"
                )

                val trip2 = Trip(
                    title = "Escapada a París",
                    destination = "París",
                    startDate = null,
                    endDate = null,
                    notes = "Ver la Torre Eiffel"
                )

                database.tripDao().insertTrip(trip1)
                database.tripDao().insertTrip(trip2)

            }
        }
    }
}