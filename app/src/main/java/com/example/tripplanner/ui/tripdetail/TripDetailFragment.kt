package com.example.tripplanner.ui.tripdetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.model.Trip
import org.w3c.dom.Text

private const val ARG_TRIP_TITLE = "arg_trip_title"
private const val ARG_TRIP_DESTINATION = "arg_trip_destination"
private const val ARG_TRIP_START_DATE = "arg_trip_start_date"
private const val ARG_TRIP_END_DATE = "arg_trip_end_date"
private const val ARG_TRIP_NOTES = "arg_trip_notes"

class TripDetailFragment : Fragment(R.layout.fragment_trip_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARG_TRIP_TITLE)
        val destination = arguments?.getString(ARG_TRIP_DESTINATION)
        val startDate = arguments?.getString(ARG_TRIP_START_DATE)
        val endDate = arguments?.getString(ARG_TRIP_END_DATE)
        val notes = arguments?.getString(ARG_TRIP_NOTES)

        val tvTitle = view.findViewById<TextView>(R.id.tvTripTitle)
        val tvDestination = view.findViewById<TextView>(R.id.tvTripDestination)
        val tvDates = view.findViewById<TextView>(R.id.tvTripDates)
        val tvNotes = view.findViewById<TextView>(R.id.tvTripNotes)

        tvTitle.text = title ?: "Sin título"
        tvDestination.text = destination ?: "Destino por definir"

        if (startDate != null && endDate !=null) {
            tvDates.text = "$startDate - $endDate"
        } else {
            tvDates.visibility = View.GONE
        }

        if (!notes.isNullOrBlank()) {
            tvNotes.text = notes
        } else {
            tvNotes.visibility = View.GONE
        }
    }

    companion object  {

        fun newInstance(trip: Trip): TripDetailFragment {
            val fragment = TripDetailFragment()

            val bundle = Bundle().apply {
                putString(ARG_TRIP_TITLE, trip.title)
                putString(ARG_TRIP_DESTINATION, trip.destination)
                putString(ARG_TRIP_START_DATE, trip.startDate)
                putString(ARG_TRIP_END_DATE, trip.endDate)
                putString(ARG_TRIP_NOTES, trip.notes)
            }

            fragment.arguments = bundle
            return fragment
        }
    }

}