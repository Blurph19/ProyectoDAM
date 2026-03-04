package com.example.tripplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip (
    @PrimaryKey val id: Int,
    val title: String,
    val destination: String,
    val startDate: String?,
    val endDate: String?,
    val notes: String?
)