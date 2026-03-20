package com.example.tripplanner.ui.budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.data.local.entity.Expense
import com.example.tripplanner.R

class BudgetAdapter(
    private var expenses: List<Expense>,
    private val onLongClick: (Expense) -> Unit
    ) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tvExpenseTitle)
            val amount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = expenses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val expense = expenses[position]

        holder.title.text = expense.title
        holder.amount.text = String.format("%.2f€", expense.amount)


        holder.itemView.setOnLongClickListener {
            onLongClick(expense)
            true
        }
    }

    fun updateExpenses(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
