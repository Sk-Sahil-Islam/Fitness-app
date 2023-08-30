package com.example.fitnessapp.data.remote

import com.example.fitnessapp.data.remote.responses.Quotes
import retrofit2.http.GET
import retrofit2.http.Path

interface FitnessApi {
    @GET("{today}")
    suspend fun getDailyQuote(
        @Path("today")
        today: String = "today"
    ): Quotes
}