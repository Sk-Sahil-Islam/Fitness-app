package com.example.fitnessapp.worker

interface AlarmScheduler {
    fun schedule()
    fun cancel()
}