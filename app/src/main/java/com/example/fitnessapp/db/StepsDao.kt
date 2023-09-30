package com.example.fitnessapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: Steps)

    @Query("SELECT * FROM steps")
    fun getSteps(): Flow<List<Steps>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSteps(steps: Steps)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentDaySteps(steps: Steps)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePreviousDaySteps(steps: Steps)

    @Query("SELECT current_day from steps WHERE day = :day")
    fun getCurrentDaySteps(day: Int): Flow<Int?>

    @Query("SELECT previous_day from steps WHERE day = :day")
    fun getPreviousDaySteps(day: Int): Flow<Int?>
}