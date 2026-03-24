package com.example.tripplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Expense(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Int,

    val title: String,

    val amount: Double
)
