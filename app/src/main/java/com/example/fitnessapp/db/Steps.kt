package com.example.fitnessapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(
    tableName = "steps"
)
data class Steps(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "day")
    val day: Int,  //values 1 (Sunday) through 7 (Saturday)

    @ColumnInfo(name = "previous_day")
    val prevDay: Int,

    @ColumnInfo(name = "current_day")
    val currentDay: Int
)