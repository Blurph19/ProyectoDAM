package com.example.tripplanner.ui.trips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.R
import com.example.tripplanner.model.Trip

class TripAdapter(
    private val trips: List<Trip>,
    private val onTripClick: (Trip) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTripName: TextView = itemView.findViewById(R.id.tvTripName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = trips[position]
        holder.tvTripName.text = trip.name

        holder.itemView.setOnClickListener {
            onTripClick(trip)
        }
    }

    override fun getItemCount(): Int = trips.size
}
