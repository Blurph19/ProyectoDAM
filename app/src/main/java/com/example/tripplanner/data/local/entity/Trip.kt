package com.example.tripplanner.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Trip (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: Int = 0,
    val title: String,
    val destination: String,
    val startDate: String?,
    val endDate: String?,
    val notes: String?,

    val status: String = "Pendiente",
    val imageUri: String? = null
) : Parcelable