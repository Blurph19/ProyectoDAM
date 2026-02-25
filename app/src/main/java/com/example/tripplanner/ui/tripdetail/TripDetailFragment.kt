package com.example.tripplanner.ui.tripdetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.model.Trip

private const val ARG_TRIP_NAME = "trip_name"
private const val ARG_TRIP_ID = "trip_id"

class TripDetailFragment : Fragment(R.layout.fragment_trip_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tripName = arguments?.getString(ARG_TRIP_NAME)

        val tripId = arguments?.getInt(ARG_TRIP_ID)

        val textView = view.findViewById<TextView>(R.id.tvDetail)
        textView.text = tripName ?: "Sin datos"

        val idTextView = view.findViewById<TextView>(R.id.tvTripId)
        idTextView.text = "ID del viaje: $tripId"

    }

    companion object  {

        fun newInstance(trip: Trip): TripDetailFragment {
            val fragment = TripDetailFragment()

            val bundle = Bundle().apply {
                putString(ARG_TRIP_NAME, trip.name)
                putInt(ARG_TRIP_ID, trip.id)
            }

            fragment.arguments = bundle
            return fragment
        }
    }

}