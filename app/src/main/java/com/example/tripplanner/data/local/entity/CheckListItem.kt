package com.example.tripplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tripplanner.data.local.entity.Trip

@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class ChecklistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Int,

    val name: String,

    val isChecked: Boolean = false
)
