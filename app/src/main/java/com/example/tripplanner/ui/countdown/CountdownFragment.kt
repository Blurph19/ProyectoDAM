package com.example.tripplanner.ui.countdown

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.data.local.database.AppDatabase
import androidx.room.Room
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.google.android.material.progressindicator.CircularProgressIndicator

class CountdownFragment : Fragment(R.layout.fragment_countdown) {

    private lateinit var database: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "trip_database"
        ).build()

        val tvTripTitleCount = view.findViewById<TextView>(R.id.tvTripTitleCount)
        val progressCountdown = view.findViewById<CircularProgressIndicator>(R.id.progressCountdown)
        val tvCountdownCenter = view.findViewById<TextView>(R.id.tvCountdownCenter)

        CoroutineScope(Dispatchers.IO).launch {

            val trips = database.tripDao().getAllTrips()

            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val today = Date()

            val tripsWithDate = trips.filter {

                if (it.startDate == null) return@filter false

                val tripDate = inputFormat.parse(it.startDate!!)
                tripDate!!.after(today)
            }

            val nextTrip = tripsWithDate.minByOrNull {

                val tripDate = inputFormat.parse(it.startDate!!)
                tripDate!!.time
            }

            if (nextTrip !=null) {

                activity?.runOnUiThread {

                    tvTripTitleCount.text = nextTrip.title

                    val tripDate = inputFormat.parse(nextTrip.startDate!!)
                    val today = Date()

                    val diff = tripDate!!.time - today.time

                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24

                    tvCountdownCenter.text = "${days}d\n${hours}h"

                    val maxCountdownDays = 30f
                    val progress = ((maxCountdownDays - days.toFloat()).coerceAtLeast(0f) / maxCountdownDays * 100).toInt()
                    progressCountdown.progress = progress
                }

            }else {

                activity?.runOnUiThread {
                    tvTripTitleCount.text = "No hay viajes"
                }
            }


        }
    }
}
