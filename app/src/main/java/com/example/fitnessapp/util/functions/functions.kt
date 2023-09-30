package com.example.fitnessapp.util.functions

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import java.text.NumberFormat

fun formateNumber(number: Int?): String {
    if(number != null) {
        return NumberFormat.getNumberInstance().format(number)
    }
    return "0"
}