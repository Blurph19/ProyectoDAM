package com.example.tripplanner.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.R
import com.example.tripplanner.ui.tripdetail.TripDetailFragment
import com.example.tripplanner.ui.trips.TripListFragment
import com.example.tripplanner.model.Trip

class MainActivity : AppCompatActivity(),
    TripListFragment.OnTripSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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