package com.example.tripplanner.ui.tripdetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tripplanner.data.local.entity.Trip
import com.example.tripplanner.ui.checklist.ChecklistFragment
import com.example.tripplanner.ui.itinerary.ItineraryFragment
import com.example.tripplanner.ui.budget.BudgetFragment

class TripDetailPagerAdapter(fragment: Fragment,
    private val trip: Trip) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> ItineraryFragment.newInstance(trip)

            1 -> BudgetFragment()

            2 -> ChecklistFragment()

            else -> ItineraryFragment.newInstance(trip)
        }
    }

}
