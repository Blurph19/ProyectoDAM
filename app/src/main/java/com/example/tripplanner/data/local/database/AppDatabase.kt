package com.example.tripplanner.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tripplanner.data.local.dao.TripDao
import com.example.tripplanner.data.local.entity.Trip

@Database(
    entities = [Trip::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
}