package com.example.tripplanner.ui.trips

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.data.local.entity.Trip
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.ui.trips.TripAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import com.example.tripplanner.ui.createtrip.CreateTripActivity
import com.example.tripplanner.ui.main.MainActivity


class TripListFragment : Fragment(R.layout.fragment_trip_list) {

    interface OnTripSelectedListener {
        fun onTripSelected(trip: Trip)
    }

    private var listener: OnTripSelectedListener? = null
    private lateinit var adapter: TripAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnTripSelectedListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAddTrip = view.findViewById<FloatingActionButton>(R.id.fabAddTrip)

        fabAddTrip.setOnClickListener {
            val intent = Intent(requireContext(), CreateTripActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerTrips)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TripAdapter(emptyList()) { trip ->
            listener?.onTripSelected(trip)
        }

        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.loadTrips()
    }

    fun updateTrips(trips: List<Trip>) {
        adapter.updateTrips(trips)
    }

}
