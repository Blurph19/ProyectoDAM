package com.example.tripplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tripplanner.data.local.entity.ChecklistItem

@Dao
interface ChecklistDao {

    @Query("SELECT * FROM checklist_items WHERE tripId = :tripId")
    suspend fun getItemsForTrip(tripId: Int): List<ChecklistItem>

    @Insert
    suspend fun insertItem(item: ChecklistItem)

    @Update
    suspend fun updateItem(item: ChecklistItem)

    @Delete
    suspend fun deleteItem(item: ChecklistItem)
}