package com.example.tripplanner.ui.createtrip

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.R
import com.example.tripplanner.data.local.database.AppDatabase
import androidx.room.Room
import com.example.tripplanner.data.local.entity.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDestination = findViewById<EditText>(R.id.etDestination)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val btnSaveTrip = findViewById<Button>(R.id.btnSaveTrip)

        val etStartDate = findViewById<EditText>(R.id.etStartDate)
        val etEndDate = findViewById<EditText>(R.id.etEndDate)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        ).build()

        btnSaveTrip.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val destination = etDestination.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            val startDate = etStartDate.text.toString().ifEmpty { null }
            val endDate = etEndDate.text.toString().ifEmpty { null }

            if (title.isEmpty()) {
                etTitle.error = "Introduce un título"
                return@setOnClickListener
            }

            if (destination.isEmpty()) {
                etDestination.error = "Introduce un destino"
                return@setOnClickListener
            }

            val trip = Trip(
                title = title,
                destination = destination,
                startDate = startDate,
                endDate = endDate,
                notes = notes
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.tripDao().insertTrip(trip)

                runOnUiThread {
                    finish()
                }

            }

        }

        etStartDate.setOnClickListener {
            showDatePicker(etStartDate)
        }

        etEndDate.setOnClickListener {
            showDatePicker(etEndDate)
        }

    }

    private fun showDatePicker(target: EditText) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->

            val formattedMonth = selectedMonth + 1
            val date = "%04d-%02d-%02d".format(selectedYear, selectedMonth, selectedDay)

            target.setText(date)

        }, year, month, day)

        datePicker.show()
    }

}