package com.example.tripplanner.ui.checklist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.R
import com.example.tripplanner.data.local.entity.ChecklistItem

class ChecklistAdapter(
    private var items: List<ChecklistItem>,
    private val onItemChecked: (ChecklistItem, Boolean) -> Unit,
    private val onLongClick: (ChecklistItem) -> Unit
) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    class ViewHolder(val checkBox: CheckBox) : RecyclerView.ViewHolder(checkBox)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checklist, parent, false) as CheckBox

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.checkBox.text = item.name
        holder.checkBox.isChecked = item.isChecked

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onItemChecked(item, isChecked)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<ChecklistItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}