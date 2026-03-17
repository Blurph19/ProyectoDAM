package com.example.tripplanner.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tripplanner.data.local.dao.TripDao
import com.example.tripplanner.data.local.entity.Trip
import com.example.tripplanner.data.local.entity.ChecklistItem
import com.example.tripplanner.data.local.dao.ChecklistDao

@Database(
    entities = [Trip::class, ChecklistItem::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
    abstract fun checklistDao(): ChecklistDao
}