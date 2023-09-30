package com.example.fitnessapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreDayInfo(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("previousDay")
        val PREVIOUS_DAY_KEY = stringPreferencesKey("previous_Day")
    }

    val getPreviousDay: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PREVIOUS_DAY_KEY] ?: "0"
        }

    suspend fun savePreviousDay(day: String) {
        context.dataStore.edit {  preferences ->
            preferences[PREVIOUS_DAY_KEY] = day
        }
    }
}