@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitnessapp.ui.home_screen

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitnessapp.R
import com.example.fitnessapp.ui.navigation_drawer.NavigationDrawerViewModel
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
import com.example.fitnessapp.util.functions.formateNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationDrawerViewModel = hiltViewModel(),
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_WEEK)

    val isLoading by navigationViewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            navigationViewModel.loadDailyQuote()
            homeViewModel.getSteps()
        }
    )

    val state = homeViewModel.stepState
    val previousDaySteps = if (day > 1) state.steps[day-2].currentDay else state.steps[6].currentDay
    val todaySteps = state.steps[day - 1].currentDay - previousDaySteps
    Log.d("steps in home", todaySteps.toString())
    var caloriesBurned by remember{ mutableIntStateOf(0) }
    caloriesBurned = homeViewModel.caloriesBurntWalking(steps = todaySteps).toInt()

    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            StatsIndicator(
                caloriesBurnt = caloriesBurned,
                dailyCaloriesTarget = 500,
                dailyStepsTarget = 20_000,
                steps = todaySteps,

            )
            Spacer(modifier = Modifier.height(24.dp))
            StatsCard(
                steps = todaySteps,
                moveMin = 118,
                caloriesBurnt = caloriesBurned,
                distanceTraveled = 11.8,
                averageSpeed = 4.83
            )
            CaloriesCard(
                consumedCalories = 268,
                dailyRequirement = 1647
            )
            WaterCard(
                consumedWaterGlasses = 4,
                requireDailyWaterGlasses = 8
            )
            SleepCard(

            )
            CaloriesBurntCard(
                dailyCalories = 834
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter)
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
            text = formateNumber(caloriesBurnt),
            fontFamily = Bricolage,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        )
        Text(
            text = formateNumber(steps),
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
    val percentage = if (currentValue < maxValue) currentValue / maxValue.toFloat() else 1f

    LaunchedEffect(percentage) {
        animateFloat.animateTo(
            targetValue = percentage,
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

@OptIn(ExperimentalMaterial3Api::class)
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
        onClick = {
            /*TODO*/
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatWithIcon(
                    modifier = Modifier.rotate(-10f),
                    value = steps,
                    icon = ImageVector.vectorResource(id = R.drawable.ic_filled_steps),
                    tint = if (isSystemInDarkTheme()) MyGreen else MyDarkerGreen
                )
                StatWithIcon(
                    value = moveMin,
                    icon = Icons.Default.AccessTimeFilled,
                    tint = if (isSystemInDarkTheme()) MyBlue else MyDarkerBlue
                )
                StatWithIcon(
                    value = caloriesBurnt,
                    icon = Icons.Default.LocalFireDepartment,
                    tint = if (isSystemInDarkTheme()) RosePink else DarkRosePink
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            StatWithUnits(
                distanceTraveled = distanceTraveled,
                averageSpeed = averageSpeed,
                unit = "km"
            )
        }
    }
}

@Composable
fun StatWithIcon(
    modifier: Modifier = Modifier,
    value: Int,
    icon: ImageVector,
    tint: Color = LocalContentColor.current
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = formateNumber(value),
            fontFamily = Bricolage,
            fontWeight = FontWeight.W600,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.size(6.dp))
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = modifier)
    }
}

@Composable
fun StatWithUnits(
    distanceTraveled: Double,
    averageSpeed: Double,
    unit: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,

            ) {
            Text(
                text = "Distance",
                style = ownTypography.bodyLarge,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "Avg. Speed",
                style = ownTypography.bodyLarge,
                fontWeight = FontWeight.W500
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = distanceTraveled.toString(),
                    fontFamily = Bricolage,
                    fontSize = 18.sp,
                    modifier = Modifier.offset(y = 3.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = unit,
                    fontSize = 11.125.sp,
                    style = ownTypography.bodyMedium,
                    color = LocalContentColor.current.copy(alpha = 0.75f),
                )
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = averageSpeed.toString(),
                    fontFamily = Bricolage,
                    fontSize = 18.sp,
                    modifier = Modifier.offset(y = 3.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "$unit/h",
                    fontSize = 11.125.sp,
                    style = ownTypography.bodyMedium,
                    color = LocalContentColor.current.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
fun CaloriesCard(
    modifier: Modifier = Modifier,
    consumedCalories: Int,
    dailyRequirement: Int
) {
    Card(
        onClick = {
            /*TODO*/
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Calories ate",
                style = ownTypography.titleLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.W600
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$consumedCalories",
                    fontFamily = Bricolage,
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    modifier = Modifier.offset(y = 3.dp),
                    text = "/$dailyRequirement kcal",
                    fontFamily = Bricolage,
                    fontSize = 15.451.sp,
                    color = LocalContentColor.current.copy(alpha = 0.75f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        //TODO:
                    }
                ) {
                    Text(text = "Add", fontSize = 16.sp)
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = "add")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterCard(
    modifier: Modifier = Modifier,
    consumedWaterGlasses: Int,
    requireDailyWaterGlasses: Int
) {
    Card(
        onClick = {
            /*TODO*/
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Water",
                style = ownTypography.titleLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.W600
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$consumedWaterGlasses",
                    fontFamily = Bricolage,
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    modifier = Modifier.offset(y = 3.dp),
                    text = "/$requireDailyWaterGlasses ",
                    fontFamily = Bricolage,
                    fontSize = 15.451.sp,
                    color = LocalContentColor.current.copy(alpha = 0.75f)
                )
                Text(
                    modifier = Modifier.offset(y = 2.dp),
                    text = if (consumedWaterGlasses <= 1) "glass" else "glasses",
                    fontFamily = Kanit,
                    fontSize = 15.451.sp,
                    color = LocalContentColor.current.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        //TODO
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RemoveCircleOutline,
                        contentDescription = "minus water",
                        modifier = Modifier.size(33.dp)
                    )
                }
                IconButton(
                    onClick = {
                        //TODO
                    },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircleOutline,
                        contentDescription = "add water",
                        modifier = Modifier.size(33.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepCard(
    modifier: Modifier = Modifier,
    hoursOfSleep: Int = 8
) {
    Card(
        onClick = {
            /*TODO*/
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Hours of sleep",
                style = ownTypography.titleLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.W600
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$hoursOfSleep",
                    fontFamily = Bricolage,
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    modifier = Modifier.offset(y = 3.dp),
                    text = "(Recommended)",
                    fontFamily = Kanit,
                    fontSize = 15.451.sp,
                    color = LocalContentColor.current.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        /*TODO*/
                    },
                ) {
                    Text(text = "Edit", fontSize = 16.sp)
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "edit",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloriesBurntCard(
    modifier: Modifier = Modifier,
    dailyCalories: Int
) {
    Card(
        onClick = {
            /*TODO*/
        },
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Energy expended",
                style = ownTypography.titleLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.W600
            )
            Text(
                text = "Last 7 days",
                style = ownTypography.bodyLarge,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.size(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$dailyCalories",
                            fontFamily = Bricolage,
                            fontSize = 25.sp
                        )
                        Spacer(modifier = Modifier.size(3.dp))
                        Text(
                            text = "Cal",
                            fontSize = 15.451.sp,
                            modifier = Modifier.offset(y = 3.dp),
                            style = ownTypography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                    }
                    Text(
                        text = "Today",
                        fontFamily = Kanit
                    )
                }

                Box(
                    modifier = Modifier
                        .size(width = 175.dp, height = 75.dp)
//                        .background(Color.Red)
                ) {
                }
            }
        }
    }
}