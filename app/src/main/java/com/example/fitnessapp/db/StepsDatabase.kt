package com.example.fitnessapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Steps::class],
    version = 1
)
abstract class StepsDatabase: RoomDatabase() {

    abstract fun dao(): StepsDao
}