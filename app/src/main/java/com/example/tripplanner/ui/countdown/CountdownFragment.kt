package com.example.tripplanner.ui.countdown

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.tripplanner.R
import com.example.tripplanner.data.local.database.AppDatabase
import androidx.room.Room
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.example.tripplanner.data.local.entity.Trip
import com.example.tripplanner.ui.tripdetail.TripDetailFragment
import com.example.tripplanner.utils.SessionManager

class CountdownFragment : Fragment(R.layout.fragment_countdown) {

    private lateinit var database: AppDatabase

    private var nextTrip: Trip? = null

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
        val btnOpenTrip = view.findViewById<Button>(R.id.btnOpenTrip)

        btnOpenTrip.setOnClickListener {

            val trip = nextTrip

            if (trip != null) {

                val detailFragment = TripDetailFragment.newInstance(trip)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {

            val userId = SessionManager.getUserId(requireContext())
            val trips = database.tripDao().getTripsByUser(userId)

            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val today = Date()

            val tripsWithDate = trips.filter {

                if (it.startDate == null) return@filter false

                val tripDate = inputFormat.parse(it.startDate!!)
                tripDate!!.after(today)
            }

            nextTrip = tripsWithDate.minByOrNull {
                val tripDate = inputFormat.parse(it.startDate!!)
                tripDate!!.time
            }

            val trip = nextTrip

            if (trip !=null) {

                activity?.runOnUiThread {

                    tvTripTitleCount.text = trip.title

                    val tripDate = inputFormat.parse(trip.startDate!!)
                    val today = Date()

                    val diff = tripDate!!.time - today.time

                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    when (days.toInt()) {
                        in 15..Int.MAX_VALUE -> {
                                progressCountdown.setIndicatorColor(Color.parseColor("#42A5F5"))
                        }
                        in 7..14 -> {
                                progressCountdown.setIndicatorColor(Color.parseColor("#FFA726"))
                        }
                        else -> {
                                progressCountdown.setIndicatorColor(Color.parseColor("#EF5350"))
                        }
                    }


                    val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24

                    tvCountdownCenter.text = "${days}d\n${hours}h"

                    val maxCountdownDays = 30f
                    val progress = ((maxCountdownDays - days.toFloat()).coerceAtLeast(0f) / maxCountdownDays * 100).toInt()
                    progressCountdown.progress = progress
                }

            } else {

                activity?.runOnUiThread {
                    tvTripTitleCount.text = "No hay viajes futuros"

                    tvCountdownCenter.text = "-"

                    progressCountdown.progress = 0
                }
            }


        }
    }
}
