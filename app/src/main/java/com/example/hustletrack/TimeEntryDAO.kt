package com.example.hustletrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeEntryDao {
    @Insert
    suspend fun insert(entry: TimeEntry)

    @Query("SELECT * FROM time_entries ORDER BY startTime DESC")
    fun getAll(): Flow<List<TimeEntry>>

    @Delete
    suspend fun delete(entry: TimeEntry)
}

