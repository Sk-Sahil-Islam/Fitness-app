package com.example.fitnessapp.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fitnessapp.repository.FitnessRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.time.Duration
import java.time.LocalTime
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)

class AndroidAlarmScheduler(
    private val context: Context,
    private val repository: FitnessRepository
) : AlarmScheduler {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val calendar = Calendar.getInstance()
    private val day = calendar.get(Calendar.DAY_OF_WEEK)

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val currentTime = LocalTime.now()
    private val midnight = LocalTime.MIDNIGHT
    private val durationUntilMidnight = Duration.between(currentTime, midnight).toMillis()

    override fun schedule() {

        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
//                LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            AlarmManager.INTERVAL_DAY,
//                10 * 1000 + 1000,
            durationUntilMidnight,
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel() {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}