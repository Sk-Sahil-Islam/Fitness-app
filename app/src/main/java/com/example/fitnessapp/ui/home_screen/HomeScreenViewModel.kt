package com.example.fitnessapp.ui.home_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.sensor.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val pedometerSensor: MeasurableSensor
): ViewModel() {

//    private val _totalSteps = MutableStateFlow(0)
//    val totalSteps = _totalSteps.asStateFlow()
    var totalSteps = mutableIntStateOf(0)
    init {
        pedometerSensor.startListening()
        pedometerSensor.setOnSensorValuesChangedListener { values ->
            val steps = values[0]
//            _totalSteps.value = steps.toInt() - 64
//            Log.e("steps", _totalSteps.value.toString())
            totalSteps.intValue = steps.toInt()
            Log.e("steps", totalSteps.intValue.toString())
        }
    }
}