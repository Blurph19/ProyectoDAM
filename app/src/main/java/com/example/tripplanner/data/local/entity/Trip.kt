package com.example.tripplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val destination: String,
    val startDate: String?,
    val endDate: String?,
    val notes: String?
)