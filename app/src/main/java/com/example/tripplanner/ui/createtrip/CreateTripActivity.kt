package com.example.tripplanner.ui.createtrip

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

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "trip_database"
        ).build()

        btnSaveTrip.setOnClickListener {
            val title = etTitle.text.toString()
            val destination = etDestination.text.toString()
            val notes = etNotes.text.toString()

            val trip = Trip(
                title = title,
                destination = destination,
                startDate = null,
                endDate = null,
                notes = notes,
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.tripDao().insertTrip(trip)

                runOnUiThread {
                    finish()
                }

            }

        }

    }

}