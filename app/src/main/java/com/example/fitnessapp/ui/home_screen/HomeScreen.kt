@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitnessapp.ui.home_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.ui.theme.Bricolage
import com.example.fitnessapp.ui.theme.DarkRosePink
import com.example.fitnessapp.ui.theme.Kanit
import com.example.fitnessapp.ui.theme.LightRosePinkGrey
import com.example.fitnessapp.ui.theme.MyBlue
import com.example.fitnessapp.ui.theme.MyDarkerBlue
import com.example.fitnessapp.ui.theme.MyDarkerGreen
import com.example.fitnessapp.ui.theme.MyGreen
import com.example.fitnessapp.ui.theme.RosePink
import com.example.fitnessapp.ui.theme.RosePinkGrey
import com.example.fitnessapp.ui.theme.ownTypography

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.size(14.dp))
        StatsIndicator(
            caloriesBurnt = 458,
            dailyCaloriesTarget = 500,
            dailyStepsTarget = 10_000,
            steps = 5_842
        )
        Spacer(modifier = Modifier.height(45.dp))
        StatsCard(
            steps = 5_842,
            moveMin = 118,
            caloriesBurnt = 458,
            distanceTraveled = 11.8,
            averageSpeed = 4.83
        )
    }

}

@Composable
fun StatsIndicator(
    modifier: Modifier = Modifier,
    caloriesBurnt: Int,
    dailyCaloriesTarget: Int,
    dailyStepsTarget: Int,
    steps: Int
) {
    val darkTheme = isSystemInDarkTheme()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressTracker(
            currentValue = steps,
            maxValue = dailyStepsTarget,
            progressBackgroundColor = MaterialTheme.colorScheme.inversePrimary,
            progressIndicatorColor = MaterialTheme.colorScheme.primary,
        )
        CircularProgressTracker(
            currentValue = caloriesBurnt,
            maxValue = dailyCaloriesTarget,
            progressBackgroundColor = if (darkTheme) RosePinkGrey else LightRosePinkGrey,
            progressIndicatorColor = if (darkTheme) RosePink else DarkRosePink,
            radius = 63.dp
        )
        Stats(caloriesBurnt = caloriesBurnt, steps = steps)
    }
}

@Composable
fun Stats(
    caloriesBurnt: Int,
    steps: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = caloriesBurnt.toString(),
            fontFamily = Bricolage,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier.offset(y = 2.dp)
        )
        Text(
            text = steps.toString(),
            fontSize = 27.sp,
            fontFamily = Bricolage,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(y = (-5).dp)
        )
    }
}

@Composable
fun CircularProgressTracker(
    modifier: Modifier = Modifier,
    currentValue: Int,
    maxValue: Int,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    radius: Dp = 80.dp,
    strokeWidth: Dp = 8.dp,
) {

    val animateFloat = remember { Animatable(0f) }

    LaunchedEffect(true) {
        animateFloat.animateTo(
            targetValue = currentValue / maxValue.toFloat(),
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = progressBackgroundColor,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round),

                )
            drawArc(
                color = progressIndicatorColor,
                startAngle = 270f,
                sweepAngle = 360f * animateFloat.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    steps: Int,
    moveMin: Int,
    caloriesBurnt: Int,
    distanceTraveled: Double,
    averageSpeed: Double
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatWithIcon(
                    value = steps,
                    icon = Icons.Default.DirectionsRun,
                    tint = if (isSystemInDarkTheme()) MyGreen else MyDarkerGreen
                )
                StatWithIcon(
                    value = moveMin,
                    icon = Icons.Default.Timelapse,
                    tint = if (isSystemInDarkTheme()) MyBlue else MyDarkerBlue
                )
                StatWithIcon(
                    value = caloriesBurnt,
                    icon = Icons.Default.LocalFireDepartment,
                    tint = if (isSystemInDarkTheme()) RosePink else DarkRosePink
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            StatWithUnits(distanceTraveled = 11.8, averageSpeed = 4.83, unit = "km")
        }
    }
}

@Composable
fun StatWithIcon(
    value: Int,
    icon: ImageVector,
    tint: Color = LocalContentColor.current
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = value.toString(), fontFamily = Bricolage, fontWeight = FontWeight.W600)
        Spacer(modifier = Modifier.size(6.dp))
        Icon(imageVector = icon, contentDescription = null, tint = tint)
    }
}

@Composable
fun StatWithUnits(
    distanceTraveled: Double,
    averageSpeed: Double,
    unit: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(
                text = "Distance Traveled : ",
                style = ownTypography.bodyLarge,
                fontWeight = FontWeight.W300
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "Average Speed :",
                style = ownTypography.bodyLarge,
                fontWeight = FontWeight.W300
            )

        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Row {
                Text(text = distanceTraveled.toString(), fontFamily = Bricolage)
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = unit,
                    style = ownTypography.bodyLarge,
                    fontWeight = FontWeight.W300
                )
            }
            Row {
                Text(text = averageSpeed.toString(), fontFamily = Bricolage)
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "$unit/h",
                    style = ownTypography.bodyLarge,
                    fontWeight = FontWeight.W300
                )
            }

        }

    }
}



