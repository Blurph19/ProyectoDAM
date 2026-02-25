package com.example.tripplanner.ui.trips

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.model.Trip
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.ui.trips.TripAdapter

class TripListFragment : Fragment(R.layout.fragment_trip_list) {

    interface OnTripSelectedListener {
        fun onTripSelected(trip: Trip)
    }

    private var listener: OnTripSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnTripSelectedListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerTrips)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = TripAdapter(trips) { trip ->
            listener?.onTripSelected(trip)
        }

        recyclerView.adapter = adapter
    }

    private val trips = listOf(
        Trip(1, "Viaje a Roma"),
        Trip (2, "Fin de semana en París"),
        Trip (3, "Ruta por Andalucía")
    )
}
