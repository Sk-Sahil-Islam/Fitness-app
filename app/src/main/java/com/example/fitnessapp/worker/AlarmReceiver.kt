package com.example.fitnessapp.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.fitnessapp.db.Steps
import com.example.fitnessapp.repository.FitnessRepository
import com.example.fitnessapp.sensor.MeasurableSensor
import com.example.fitnessapp.ui.home_screen.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: FitnessRepository

    @Inject
    lateinit var pedometerSensor: MeasurableSensor

    private val calendar = Calendar.getInstance()
    private val day = calendar.get(Calendar.DAY_OF_WEEK)

    override fun onReceive(context: Context?, intent: Intent?) {

        //val currentDaySteps = intent?.getIntExtra("EXTRA_CURRENT_DAY", 0) ?: return
        if (::pedometerSensor.isInitialized && ::repository.isInitialized) {
            println("both are initialized")
            pedometerSensor.startListening()
            pedometerSensor.setOnSensorValuesChangedListener { values ->
                repository.updatePreviousSteps(steps = Steps(day = day, prevDay = values[0].toInt(), currentDay = 0))
            }
        }
    }
}