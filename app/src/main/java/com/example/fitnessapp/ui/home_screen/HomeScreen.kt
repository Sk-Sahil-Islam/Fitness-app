package com.example.fitnessapp.ui.home_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.ui.theme.Bricolage
import com.example.fitnessapp.ui.theme.Purple80
import com.example.fitnessapp.ui.theme.PurpleGrey40
import com.example.fitnessapp.ui.theme.trackerCaloriesColor
import com.example.fitnessapp.ui.theme.trackerGrey

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        StatsIndicator(
            caloriesBurnt = 458,
            dailyCaloriesTarget = 500,
            dailyStepsTarget = 10_000,
            steps = 5_842
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressTracker(
            currentValue = steps,
            maxValue = dailyStepsTarget,
            progressBackgroundColor = trackerGrey,
            progressIndicatorColor = MaterialTheme.colorScheme.primary,
        )
        CircularProgressTracker(
            currentValue = caloriesBurnt,
            maxValue = dailyCaloriesTarget,
            progressBackgroundColor = trackerGrey,
            progressIndicatorColor = trackerCaloriesColor,
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
            fontSize = 20.sp
        )
        Text(
            text = steps.toString(),
            fontSize = 30.sp,
            fontFamily = Bricolage,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun CircularProgressTracker(
    modifier: Modifier = Modifier,
//    percentage: Float,
//    initialValue: Float = 0f,
    currentValue: Int,
    maxValue: Int,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    radius: Dp = 80.dp,
//    completedColor: Color,
    strokeWidth: Dp = 8.dp,
//    animDelay: Int = 0
) {
//    var size by remember {
//        mutableStateOf(IntSize.Zero)
//    }
//    var animationPlayed by remember {
//        mutableStateOf(false)
//    }
//    var value by remember {
//        mutableFloatStateOf(initialValue)
//    }
//    var curPercentage = animateFloatAsState(
//        targetValue = if (animationPlayed) percentage else 0f,
//        animationSpec = tween(
//            durationMillis = 2000,
//            delayMillis = animDelay
//        ), label = ""
//    )
//    LaunchedEffect(key1 = true){
//        animationPlayed = true
//    }

    val animateFloat = remember { Animatable(0f) }

    LaunchedEffect(true){
        animateFloat.animateTo(
            targetValue = currentValue / maxValue.toFloat(),
            animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
        )
    }

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ){
        Canvas( modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = progressBackgroundColor,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
//                size = Size(width = size.width.toFloat(), size.height.toFloat()),
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


