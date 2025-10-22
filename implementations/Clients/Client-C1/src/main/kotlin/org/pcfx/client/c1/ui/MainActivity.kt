package org.pcfx.client.c1.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Topic
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import org.pcfx.client.c1.ui.screens.ActivityFeedScreen
import org.pcfx.client.c1.ui.screens.EventsScreen
import org.pcfx.client.c1.ui.screens.InsightsScreen
import org.pcfx.client.c1.ui.screens.SettingsScreen
import org.pcfx.client.c1.ui.screens.StatisticsScreen
import org.pcfx.client.c1.ui.theme.ClientC1Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientC1Theme {
                MainScreen()
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)

@Composable
fun MainScreen() {
    val navigationItems = listOf(
        NavigationItem(
            label = "Feed",
            icon = Icons.Outlined.Feed,
            screen = { ActivityFeedScreen() }
        ),
        NavigationItem(
            label = "Events",
            icon = Icons.Outlined.Topic,
            screen = { EventsScreen() }
        ),
        NavigationItem(
            label = "Insights",
            icon = Icons.Outlined.Lightbulb,
            screen = { InsightsScreen() }
        ),
        NavigationItem(
            label = "Stats",
            icon = Icons.Outlined.Analytics,
            screen = { StatisticsScreen() }
        ),
        NavigationItem(
            label = "Settings",
            icon = Icons.Outlined.Settings,
            screen = { SettingsScreen() }
        )
    )

    val selectedTabIndex = remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTabIndex.intValue == index,
                        onClick = { selectedTabIndex.intValue = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors()
                    )
                }
            }
        }
    ) { innerPadding ->
        val currentScreen = navigationItems[selectedTabIndex.intValue]
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            currentScreen.screen()
        }
    }
}
