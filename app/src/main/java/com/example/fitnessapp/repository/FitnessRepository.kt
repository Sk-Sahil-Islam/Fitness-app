package com.example.fitnessapp.repository

import com.example.fitnessapp.data.remote.FitnessApi
import com.example.fitnessapp.data.remote.responses.Quotes
import com.example.fitnessapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FitnessRepository @Inject constructor(
    private val api: FitnessApi
) {
    suspend fun getDailyQuote(): Resource<Quotes> {
        val response = try {
            api.getDailyQuote()
        } catch (e: Exception) {
            return Resource.Error(message = e.toString())
        }
        return Resource.Success(response)
    }
}