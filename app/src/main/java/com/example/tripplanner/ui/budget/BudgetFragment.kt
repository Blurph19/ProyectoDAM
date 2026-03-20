package com.example.tripplanner.ui.budget

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.app.AlertDialog
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.tripplanner.R
import com.example.tripplanner.data.local.database.AppDatabase
import com.example.tripplanner.data.local.entity.Expense
import com.example.tripplanner.data.local.entity.Trip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetFragment : Fragment (R.layout.fragment_budget) {

    private lateinit var trip: Trip
    private lateinit var database: AppDatabase
    private lateinit var adapter: BudgetAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = requireArguments().getParcelable("trip")!!

        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "trip_database"
        ).fallbackToDestructiveMigration().build()


        val recycler = view.findViewById<RecyclerView>(R.id.recyclerExpenses)

        adapter = BudgetAdapter(emptyList()) { expense ->

            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar gasto")
                .setMessage("¿Estás seguro de que quieres eliminar este gasto?")
                .setPositiveButton("Eliminar") { _, _ ->

                    CoroutineScope(Dispatchers.IO).launch {

                        database.expenseDao().deleteExpense(expense)

                        loadExpenses()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        loadExpenses()

        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddExpense)

        fab.setOnClickListener {

            val inputTitle = EditText(requireContext())
            inputTitle.hint = "Concepto"

            val inputAmount = EditText(requireContext())
            inputAmount.hint = "Cantidad"
            inputAmount.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(inputTitle)
            layout.addView(inputAmount)

            AlertDialog.Builder(requireContext())
                .setTitle("Nuevo gasto")
                .setView(layout)
                .setPositiveButton("Añadir") { _, _ ->

                    val title = inputTitle.text.toString()
                    val amount = inputAmount.text.toString().toDoubleOrNull() ?: 0.0

                    val expense = Expense(
                        tripId = trip.id,
                        title = title,
                        amount = amount
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        database.expenseDao().insertExpense(expense)
                        loadExpenses()
                    }

                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

    }

    private fun loadExpenses() {

        CoroutineScope(Dispatchers.IO).launch {

            val expenses = database.expenseDao().getExpensesForTrip(trip.id)

            activity?.runOnUiThread {

                adapter.updateExpenses(expenses)

                val total = expenses.sumOf { it.amount }

                val tvTotal = view?.findViewById<TextView>(R.id.tvTotal)
                tvTotal?.text = "Total: ${total}€"
            }
        }
    }

    companion object {

        fun newInstance(trip: Trip): BudgetFragment {

            val fragment = BudgetFragment()

            val bundle = Bundle()
            bundle.putParcelable("trip", trip)

            fragment.arguments = bundle

            return fragment
        }
    }

}