package com.joaoreis.catbreeds.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<Screen>) {
    val navigationSelectedItem = remember { mutableStateOf(0) }

    NavigationBar {
        items.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = index == navigationSelectedItem.value,
                label = {
                    Text(stringResource(id = navigationItem.resourceId))
                },
                icon = { },
                onClick = {
                    navigationSelectedItem.value = index
                    navController.navigate(navigationItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}