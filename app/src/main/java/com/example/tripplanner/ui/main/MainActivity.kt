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

class MainActivity : AppCompatActivity(),
    TripListFragment.OnTripSelectedListener {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        ).build()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TripListFragment())
                .commit()
        }
    }

    override fun onTripSelected(trip: Trip) {

        val detailFragment = TripDetailFragment.newInstance(trip)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()

    }

}