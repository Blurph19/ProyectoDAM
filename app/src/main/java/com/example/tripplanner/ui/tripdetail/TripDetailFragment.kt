package com.example.tripplanner.ui.tripdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tripplanner.R
import com.example.tripplanner.data.local.entity.Trip
import androidx.room.Room
import com.example.tripplanner.data.local.database.AppDatabase
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripDetailFragment : Fragment(R.layout.fragment_trip_detail) {

    private lateinit var trip: Trip
    private lateinit var database: AppDatabase

    private lateinit var imageView: ImageView

    private var imageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                imageView.setImageURI(it)

                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                trip.imageUri = it.toString()

                lifecycleScope.launch {
                    database.tripDao().updateTrip(trip)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = requireArguments().getParcelable("trip")!!

        database = Room.databaseBuilder(
            requireContext(), AppDatabase::class.java, "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val etTitle = view.findViewById<TextView>(R.id.etTripTitle)
        etTitle.setText(trip.title)

        // ViewPager
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayoutTrip)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPagerTrip)

        val adapter = TripDetailPagerAdapter(this, trip)

        viewPager.adapter = adapter

        viewPager.setPageTransformer { page, position ->

            val absPos = kotlin.math.abs(position)

            page.alpha = 0.7f + (1 - absPos) * 0.3f
            page.scaleY = 0.9f + (1 - absPos) * 0.1f
        }

        val imgTripHeader = view.findViewById<ImageView>(R.id.imgTripHeader)

        if (trip.imageUri != null) {
            imgTripHeader.setImageURI(Uri.parse(trip.imageUri))
        } else {
            imgTripHeader.setImageResource(R.drawable.ic_travel_placeholder)
        }

        //Editar/agregar imagen

        imageView = view.findViewById(R.id.imgTripHeader)

        val btnEditImage = view.findViewById<ImageButton>(R.id.btnEditPhoto)

        btnEditImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Itinerario"
                    tab.setIcon(R.drawable.ic_tab_map)
                }
                1 -> {
                    tab.text = "Presupuesto"
                    tab.setIcon(R.drawable.ic_tab_budget)
                }
                2 -> {
                    tab.text = "Checklist"
                    tab.setIcon(R.drawable.ic_tab_checklist)
                }
            }
        }.attach()

        // Edit title
        etTitle.setOnFocusChangeListener { _, hasFocus ->

            if (!hasFocus) {
                val newTitle = etTitle.text.toString()

                if (newTitle != trip.title) {

                    val updatedTrip = trip.copy(title = newTitle)

                    CoroutineScope(Dispatchers.IO).launch {
                        database.tripDao().updateTrip(updatedTrip)

                        trip = updatedTrip
                    }
                }
            }
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

}
