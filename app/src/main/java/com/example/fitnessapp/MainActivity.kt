package com.example.fitnessapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.repository.FitnessRepository
import com.example.fitnessapp.ui.home_screen.HomeScreen
import com.example.fitnessapp.ui.navigation_drawer.NavigationDrawerViewModel
import com.example.fitnessapp.ui.navigation_drawer.NavigationItems
import com.example.fitnessapp.ui.theme.FitnessAppTheme
import com.example.fitnessapp.ui.theme.Kanit
import com.example.fitnessapp.ui.theme.JosefinSans
import com.example.fitnessapp.worker.AndroidAlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: FitnessRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scheduler = AndroidAlarmScheduler(this, repository)
        scheduler.schedule()

        setContent {
            FitnessAppTheme {
                val items = listOf(
                    NavigationItems(
                        route = "food",
                        title = "Food",
                        selectedIcon = Icons.Default.Fastfood,
                        unselectedIcon = Icons.Outlined.Fastfood
                    ),
                    NavigationItems(
                        route = "calories",
                        title = "Calories",
                        selectedIcon = Icons.Default.LocalFireDepartment,
                        unselectedIcon = Icons.Outlined.LocalFireDepartment
                    ),
                    NavigationItems(
                        route = "water",
                        title = "Water",
                        selectedIcon = Icons.Default.WaterDrop,
                        unselectedIcon = Icons.Outlined.WaterDrop
                    )
                )

                val viewModel: NavigationDrawerViewModel by viewModels()
//                val navController = rememberNavController()
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val isLoading by viewModel.isLoading.collectAsState()

                val quote by remember { viewModel.quote }

                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet(modifier = Modifier.width(315.dp)) {
                                Spacer(modifier = Modifier.height(6.dp))
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "close"
                                    )
                                }
                                Spacer(modifier = Modifier.size(5.dp))
                                if (quote.q == "error" && !isLoading) {
                                    MotivationalQuotes(
                                        quote = "Looks like you're in need of a virtual bridge to the digital realm." +
                                                " Don't worry," +
                                                " I'll be here whenever you're ready to reconnect.",
                                        author = "Dev. team"
                                    )
                                }
                                if (isLoading) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                                if(quote.q.isNotEmpty() && quote.a.isNotEmpty() && !isLoading) {
                                    MotivationalQuotes(quote = quote.q, author = quote.a)
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 16.dp)
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(
                                                text = item.title,
                                                fontFamily = Kanit,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        },
                                        //selected = item.route == navBackStackEntry?.destination?.route,
                                        selected = index == selectedItemIndex,
                                        onClick = {
//                                            navController.navigate(item.route) {
//                                                popUpTo(navController.graph.findStartDestination().id)
//                                                launchSingleTop = true
//                                            }
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        badge = {
                                            item.badgeCount?.let {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                NavigationDrawerItem(
                                    label = {
                                        Text(
                                            text = "Help",
                                            fontFamily = Kanit,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    selected = selectedItemIndex == 3,
                                    onClick = {
                                        selectedItemIndex = 3
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedItemIndex == 3) {
                                                Icons.Default.Help
                                            } else Icons.Outlined.HelpOutline,
                                            contentDescription = "help"
                                        )
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(topBar = {
                            TopAppBar(title = {
                                Text(
                                    text = "STEP SYNC",
                                    fontFamily = Kanit,
                                    fontWeight = FontWeight.ExtraLight
                                )
                            },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Menu,
                                            contentDescription = "menu"
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
//                                            navController.navigate("settings") {
//                                                popUpTo(navController.graph.findStartDestination().id)
//                                                launchSingleTop = true
//                                            }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Settings,
                                            contentDescription = "settings"
                                        )
                                    }
                                    IconButton(onClick = {
//                                            navController.navigate("Profile") {
//                                                popUpTo(navController.graph.findStartDestination().id)
//                                                launchSingleTop = true
//                                            }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.AccountCircle,
                                            contentDescription = "settings",
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            )
                        }) {
                            HomeScreen(modifier = Modifier.padding(it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MotivationalQuotes(
    modifier: Modifier = Modifier,
    quote: String,
    author: String
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 15.dp)
    ) {
        Column(modifier.fillMaxWidth()) {
            Text(
                text = "\"$quote\"",
                fontSize = 22.sp,
                fontFamily = JosefinSans,
                fontWeight = FontWeight.W300,
                //fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "~ $author",
                modifier
                    .align(Alignment.End)
                    .offset(x = -(15).dp),
                fontFamily = Kanit,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                fontSize = 19.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }
    }
}
