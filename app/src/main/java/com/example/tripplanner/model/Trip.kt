package com.example.tripplanner.model

data class Trip (
    val id: Int,
    val title: String,
    val destination: String,
    val startDate: String?,
    val endDate: String?,
    val notes: String?
)