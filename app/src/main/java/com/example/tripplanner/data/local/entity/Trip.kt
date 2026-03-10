package com.example.tripplanner.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Trip (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val destination: String,
    val startDate: String?,
    val endDate: String?,
    val notes: String?
) : Parcelable