package com.example.tripplanner.ui.tripdetail

import android.app.DatePickerDialog
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
import java.util.Calendar
import android.view.MotionEvent
import java.util.Date
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale

class TripDetailFragment : Fragment(R.layout.fragment_trip_detail) {

    private lateinit var trip: Trip
    private lateinit var database: AppDatabase

    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = requireArguments().getParcelable("trip")!!

        database = Room.databaseBuilder(
            requireContext(), AppDatabase::class.java, "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val etTitle = view.findViewById<TextView>(R.id.etTripTitle)
        val etDestination = view.findViewById<TextView>(R.id.etTripDestination)
        val etNotes = view.findViewById<TextView>(R.id.etTripNotes)

        val etStartDate = view.findViewById<TextView>(R.id.etStartDate)
        val etEndDate = view.findViewById<TextView>(R.id.etEndDate)

        val btnSaveChanges = view.findViewById<Button>(R.id.btnSaveChanges)
        val btnDeleteTrip = view.findViewById<Button>(R.id.btnDeleteTrip)


        // Cargar datos actuales
        etTitle.setText(trip.title)
        etDestination.setText(trip.destination)
        etNotes.setText(trip.notes)

        trip.startDate?.let {
            etStartDate.setText(formatDate(it))
        }

        trip.endDate?.let {
            etEndDate.setText(formatDate(it))
        }


        // Selector fecha inicio
        etStartDate.setOnClickListener {

            val currentDate = selectedStartDate ?: trip.startDate

            openDatePicker(currentDate) { selectedDate ->
                selectedStartDate = selectedDate
                etStartDate.setText(formatDate(selectedDate))
            }
        }

        // Selector fecha fin
        etEndDate.setOnClickListener {

            val currentDate = selectedEndDate ?: trip.endDate

            openDatePicker(currentDate) { selectedDate ->
                selectedEndDate = selectedDate
                etEndDate.setText(formatDate(selectedDate))
            }
        }

        // Guardar Cambios
        btnSaveChanges.setOnClickListener {

            val updatedTitle = etTitle.text.toString()
            val updatedDestination = etDestination.text.toString()
            val updatedNotes = etNotes.text.toString()

            val start = selectedStartDate ?: trip.startDate
            val end = selectedEndDate ?: trip.endDate

            if (start != null && end != null) {

                val startDate = parseDate(start)
                val endDate = parseDate(end)

                if (startDate != null && endDate != null && endDate.before(startDate)) {

                    Toast.makeText(
                        requireContext(),
                        "La fecha de fin no puede ser anterior a la de inicio",
                        Toast.LENGTH_LONG
                    ).show()

                    return@setOnClickListener
                }
            }

            val updatedTrip = trip.copy(
                title = updatedTitle,
                destination = updatedDestination,
                startDate = selectedStartDate ?: trip.startDate,
                endDate = selectedEndDate ?: trip.endDate,
                notes = updatedNotes
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.tripDao().updateTrip(updatedTrip)

                activity?.runOnUiThread {
                    parentFragmentManager.popBackStack()
                }
            }
        }

        //Eliminar viaje
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

    private fun openDatePicker(initialDate: String?, onDateSelected: (String) -> Unit) {

        val calendar = Calendar.getInstance()

        if (initialDate != null) {

            val parsedDate = parseDate(initialDate)

            if (parsedDate != null) {
                calendar.time = parsedDate
            }
        }

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->

                val selectedDate = String.format(
                    "%04d-%02d-%02d",
                    year,
                    month + 1,
                    day
                )

                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    private fun formatDate(date: String): String {

        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())

        val parsedDate = inputFormat.parse(date)
        return parsedDate?.let(outputFormat::format) ?: date
    }

    private fun parseDate(date: String): Date? {

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(date)

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
}
