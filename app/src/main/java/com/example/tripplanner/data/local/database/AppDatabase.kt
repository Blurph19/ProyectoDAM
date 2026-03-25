package com.example.tripplanner.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tripplanner.data.local.dao.TripDao
import com.example.tripplanner.data.local.entity.Trip
import com.example.tripplanner.data.local.entity.ChecklistItem
import com.example.tripplanner.data.local.dao.ChecklistDao
import com.example.tripplanner.data.local.dao.ExpenseDao
import com.example.tripplanner.data.local.dao.UserDao
import com.example.tripplanner.data.local.entity.Expense
import com.example.tripplanner.data.local.entity.User

@Database(
    entities = [User::class, Trip::class, ChecklistItem::class, Expense::class],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun expenseDao(): ExpenseDao

}