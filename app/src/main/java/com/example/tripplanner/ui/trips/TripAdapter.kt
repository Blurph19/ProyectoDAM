package com.example.tripplanner.ui.trips

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.R
import com.example.tripplanner.data.local.entity.Trip
import com.example.tripplanner.ui.tripdetail.TripDetailFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripAdapter(
    private var trips: List<Trip>,
    private val onTripClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTripName: TextView = itemView.findViewById(R.id.tvTripName)

        val tvTripSubtitle: TextView = itemView.findViewById(R.id.tvTripSubtitle)

        val tvStatus: TextView = itemView.findViewById(R.id.tvTripStatus)

        val imgTrip: ImageView = itemView.findViewById(R.id.imgTrip)

        val btnTripDetails: Button = itemView.findViewById<Button>(R.id.btnTripDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.tvTripName.text = trip.title

        holder.tvTripSubtitle.text = trip.destination

        if (trip.imageUri != null) {
            holder.imgTrip.setImageURI(Uri.parse(trip.imageUri))
        } else {
            holder.imgTrip.setImageResource(R.drawable.travel_placeholder)
        }


        holder.btnTripDetails.setOnClickListener {

            val detailFragment = TripDetailFragment()

            val bundle = Bundle()
            bundle.putParcelable("trip", trip)

            detailFragment.arguments = bundle

            (holder.itemView.context as AppCompatActivity)
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val status = when {
            trip.startDate == null -> "Pendiente"
            trip.startDate > today -> "Próximo"
            trip.endDate != null && trip.endDate < today -> "Finalizado"
            else -> "En curso"
        }

        holder.tvStatus.text = status

        when (status) {

            "Pendiente" ->
                holder.tvStatus.setBackgroundColor(Color.GRAY)

            "Próximo" ->
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"))

            "En curso" ->
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"))

            "Finalizado" ->
                holder.tvStatus.setBackgroundColor(Color.parseColor("#607D8B"))
        }

        holder.tvStatus.text = status.uppercase()

    }

    override fun getItemCount(): Int = trips.size

    fun updateTrips(newTrips: List<Trip>) {
        trips = newTrips
        notifyDataSetChanged()
    }
}
