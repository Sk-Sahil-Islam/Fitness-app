package com.example.fitnessapp.ui.home_screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.data.StepState
import com.example.fitnessapp.data.StoreDayInfo
import com.example.fitnessapp.db.Steps
import com.example.fitnessapp.repository.FitnessRepository
import com.example.fitnessapp.sensor.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val pedometerSensor: MeasurableSensor,
    private val repository: FitnessRepository,
    @ApplicationContext context: Context
) : ViewModel() {
    var stepState by mutableStateOf(StepState())
        private set

    private val dataStore = StoreDayInfo(context)

    private val calendar = Calendar.getInstance()
    private val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
    private var prevDay = 0

    init {
        viewModelScope.launch {
            dataStore.getPreviousDay.collectLatest {
                prevDay = it?.toInt() ?: 0
            }
        }

    }

    private var isTableEmpty by mutableStateOf(true)

    private var prevDayCurrentSteps = 0

    init {
        getSteps()
        if(currentDay > 1) {
            getPreviousCurrentSteps(currentDay - 1)
        } else {
            getPreviousCurrentSteps(7)
        }
    }

    init {
        viewModelScope.launch {
            if (prevDay != currentDay && !isTableEmpty) {
                dataStore.savePreviousDay(currentDay.toString())
//                updatePreviousSteps(steps = stepState.steps[currentDay - 1], prevDay = stepState.steps[prevDay - 1].currentDay)
                updateCurrentSteps(steps = stepState.steps, day = currentDay, currentDay = 0)
            }
            delay(500)
            if (isTableEmpty) {
                repeat(7) {
                    insertSteps(day = it + 1)
                }
            }
            Log.d("previous current day", prevDayCurrentSteps.toString())
            pedometerSensor.startListening()
            pedometerSensor.setOnSensorValuesChangedListener { values ->
                val steps = values[0]
                if (steps - prevDayCurrentSteps >= 0f) {
                    updateCurrentSteps(
                        steps = stepState.steps,
                        day = currentDay,
                        currentDay = (steps.toInt())
                    )
                } else {
                    resetCurrentSteps(steps = stepState.steps, currentDay = steps.toInt())
                }
            }
        }

    }

    private fun resetCurrentSteps(steps: List<Steps>, currentDay: Int) {
        viewModelScope.launch {
            steps.forEach { step ->
                repository.updateSteps(
                    steps = step.copy(currentDay = currentDay)
                )
            }
        }
    }

    private fun updateCurrentSteps(steps: List<Steps>, day: Int, currentDay: Int) {
        viewModelScope.launch {
            repository.updateSteps(
                steps = steps[day - 1].copy(currentDay = currentDay)
            )
        }
    }

    private fun insertSteps(day: Int) {
        viewModelScope.launch {
            repository.insertSteps(
                steps = Steps(day = day, prevDay = 0, currentDay = 0)
            )
        }
    }

    private fun getPreviousCurrentSteps(day: Int) {
        viewModelScope.launch {
            repository.getCurrentDaySteps(day).collectLatest {
                prevDayCurrentSteps = it ?: 0
            }
        }
    }

    fun getSteps() {
        viewModelScope.launch {
            repository.getSteps().collectLatest {
                isTableEmpty = it.isEmpty()
                if (it.isNotEmpty()) {
                    stepState = stepState.copy(
                        steps = it
                    )
                }
            }
        }
    }
}
