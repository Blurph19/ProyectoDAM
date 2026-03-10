package com.example.tripplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tripplanner.data.local.entity.Trip

@Dao
interface TripDao {

    @Insert
    suspend fun insertTrip(trip: Trip)

    @Query("SELECT * FROM Trip")
    suspend fun getAllTrips(): List<Trip>

    @Update
    suspend fun updateTrip(trip: Trip)
}