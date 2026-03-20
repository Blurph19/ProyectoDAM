package com.example.tripplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Expense(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tripId: Int,

    val title: String,

    val amount: Double
)
