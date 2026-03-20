package com.example.tripplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tripplanner.data.local.entity.Expense

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM Expense WHERE tripId = :tripId")
    suspend fun getExpensesForTrip(tripId: Int): List<Expense>

    @Delete
    suspend fun deleteExpense(expense: Expense)
}