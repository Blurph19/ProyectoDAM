package com.example.tripplanner.ui.tripdetail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.data.local.entity.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.tripplanner.data.local.database.AppDatabase
import androidx.appcompat.app.AlertDialog

class TripDetailFragment : Fragment(R.layout.fragment_trip_detail) {

    private lateinit var trip: Trip
    private lateinit var database: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = requireArguments().getParcelable("trip")!!

        database = Room.databaseBuilder(
            requireContext(), AppDatabase::class.java, "trip_database"
        ).build()

        val etTitle = view.findViewById<TextView>(R.id.etTripTitle)
        val etDestination = view.findViewById<TextView>(R.id.etTripDestination)
        val tvDates = view.findViewById<TextView>(R.id.tvTripDates)
        val etNotes = view.findViewById<TextView>(R.id.etTripNotes)

        val btnSaveChanges = view.findViewById<Button>(R.id.btnSaveChanges)
        val btnDeleteTrip = view.findViewById<Button>(R.id.btnDeleteTrip)


        etTitle.setText(trip.title)
        etDestination.setText(trip.destination)
        etNotes.setText(trip.notes)

        when {
            trip.startDate != null && trip.endDate != null -> {
                tvDates.text = "${formatDate(trip.startDate!!)} – ${formatDate(trip.endDate!!)}"
            }

            trip.startDate != null -> {
                tvDates.text = "Desde ${formatDate(trip.startDate!!)}"
            }

            trip.endDate != null -> {
                tvDates.text = "Hasta ${formatDate(trip.endDate!!)}"
            }

            else -> {
                tvDates.visibility = View.GONE
            }
        }

        btnSaveChanges.setOnClickListener {

            val updatedTitle = etTitle.text.toString()
            val updatedDestination = etDestination.text.toString()
            val updatedNotes = etNotes.text.toString()

            val updatedTrip = trip.copy(
                title = updatedTitle, destination = updatedDestination, notes = updatedNotes
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.tripDao().updateTrip(updatedTrip)

                activity?.runOnUiThread {
                    parentFragmentManager.popBackStack()
                }
            }
        }

        btnDeleteTrip.setOnClickListener {

            AlertDialog.Builder(requireContext()).setTitle("Eliminar viaje")
                .setMessage("¿Seguro que quieres eliminar este viaje?")
                .setPositiveButton("Eliminar") { _, _ ->

                    CoroutineScope(Dispatchers.IO).launch {

                        database.tripDao().deleteTrip(trip)

                        activity?.runOnUiThread {
                            parentFragmentManager.popBackStack()
                        }
                    }
                }.setNegativeButton("Cancelar", null).show()
        }

    }

    companion object {

        fun newInstance(trip: Trip): TripDetailFragment {
            val fragment = TripDetailFragment()

            val bundle = Bundle().apply {
                putParcelable("trip", trip)
            }

            fragment.arguments = bundle
            return fragment
        }
    }

    private fun formatDate(date: String): String {

        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())

        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate!!)
    }
}