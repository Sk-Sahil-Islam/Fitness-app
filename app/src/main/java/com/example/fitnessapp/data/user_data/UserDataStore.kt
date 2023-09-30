package com.example.fitnessapp.data.user_data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userDetails")
        val WEIGHT = stringPreferencesKey("weight")
        val HEIGHT = stringPreferencesKey("height")
        val AGE = stringPreferencesKey("age")
        val SEX = stringPreferencesKey("sex")
    }

    suspend fun saveUserDetails(userDetails: UserDetails) {
        context.dataStore.edit {
            it[WEIGHT] = userDetails.weight
            it[HEIGHT] = userDetails.height
            it[AGE] = userDetails.age
            it[SEX] = userDetails.sex
        }
    }

    val getUserDetails = context.dataStore.data.map {
        UserDetails(
            weight = it[WEIGHT] ?: "70",
            height = it[HEIGHT] ?: "170",
            age = it[AGE] ?: "27",
            sex = it[SEX] ?: "male"
        )
    }
}