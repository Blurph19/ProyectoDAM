package com.example.tripplanner.ui.checklist

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.R
import com.example.tripplanner.data.local.entity.ChecklistItem
import androidx.room.Room
import com.example.tripplanner.data.local.database.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.tripplanner.data.local.entity.Trip

class ChecklistFragment : Fragment(R.layout.fragment_checklist) {

    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChecklistAdapter

    private lateinit var trip: Trip

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = requireArguments().getParcelable("trip")!!

        recyclerView = view.findViewById(R.id.recyclerChecklist)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ChecklistAdapter(
            emptyList(),

            { item, checked ->

                CoroutineScope(Dispatchers.IO).launch {

                    val updatedItem = item.copy(isChecked = checked)

                    database.checklistDao().updateItem(updatedItem)
                }
            },

            { item ->

                AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar item")
                    .setMessage("¿Estás seguro de que quieres eliminar este elemento?")
                    .setPositiveButton("Eliminar") { _, _ ->

                        CoroutineScope(Dispatchers.IO).launch {

                            database.checklistDao().deleteItem(item)

                            loadChecklist()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        recyclerView.adapter = adapter

        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "trip_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        loadChecklist()

        val fabAddItem = view.findViewById<FloatingActionButton>(R.id.fabAddItem)

        fabAddItem.setOnClickListener {

            val editText = EditText(requireContext())

            AlertDialog.Builder(requireContext())
                .setTitle("Nuevo item")
                .setView(editText)
                .setPositiveButton("Añadir") { _, _ ->

                    val text = editText.text.toString()

                    if (text.isNotBlank()) {

                        CoroutineScope(Dispatchers.IO).launch {

                            val item = ChecklistItem(
                                tripId = trip.id,
                                name = text
                            )

                            database.checklistDao().insertItem(item)

                            loadChecklist()
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun loadChecklist() {

        CoroutineScope(Dispatchers.IO).launch {

            val items = database.checklistDao().getItemsForTrip(trip.id)

            activity?.runOnUiThread {
                adapter.updateItems(items)
            }
        }
    }

    companion object {

        fun newInstance(trip: Trip): ChecklistFragment {

            val fragment = ChecklistFragment()

            val bundle = Bundle()
            bundle.putParcelable("trip", trip)

            fragment.arguments = bundle

            return fragment
        }
    }
}