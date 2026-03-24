package com.example.tripplanner.data.local.entity

import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val email: String,

    val password: String
)
