package com.example.tripplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")

data class ChecklistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Int,

    val name: String,

    val isChecked: Boolean = false
)
