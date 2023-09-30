package com.example.fitnessapp.repository

import com.example.fitnessapp.data.remote.FitnessApi
import com.example.fitnessapp.data.remote.responses.Quotes
import com.example.fitnessapp.db.Steps
import com.example.fitnessapp.db.StepsDao
import com.example.fitnessapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class FitnessRepository @Inject constructor(
    private val api: FitnessApi,
    private val dao: StepsDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    suspend fun getDailyQuote(): Resource<Quotes> {
        val response = try {
            api.getDailyQuote()
        } catch (e: Exception) {
            return Resource.Error(message = e.toString())
        }
        return Resource.Success(response)
    }

    fun getSteps() = dao.getSteps()
    suspend fun insertSteps(steps: Steps) {
        dao.insertSteps(steps = steps)
    }

    suspend fun updateSteps(steps: Steps) {
        dao.updateSteps(steps = steps)
    }

    fun updatePreviousSteps(steps: Steps) {
        coroutineScope.launch {
            dao.updatePreviousDaySteps(steps)
        }
    }

    fun updateCurrentSteps(steps: Steps) {
        coroutineScope.launch {
            dao.updateCurrentDaySteps(steps)
        }
    }

    fun getCurrentDaySteps(day: Int) = dao.getCurrentDaySteps(day)
    fun getPreviousDaySteps(day: Int) = dao.getPreviousDaySteps(day)

}