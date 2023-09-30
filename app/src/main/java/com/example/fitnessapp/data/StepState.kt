package com.example.fitnessapp.data

import com.example.fitnessapp.db.Steps

data class StepState(
    val steps: List<Steps> = listOf(
        Steps(1, 0, 0),
        Steps(2, 0, 0),
        Steps(3, 0, 0),
        Steps(4, 0, 0),
        Steps(5, 0, 0),
        Steps(6, 0, 0),
        Steps(7, 0, 0),
    )
)
