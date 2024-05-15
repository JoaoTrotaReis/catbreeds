package com.joaoreis.catbreeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joaoreis.catbreeds.catbreedlist.ui.AllCatBreedsListScreen
import com.joaoreis.catbreeds.details.ui.CatBreedDetailsScreen
import com.joaoreis.catbreeds.favorites.ui.FavoriteCatBreedsScreen
import com.joaoreis.catbreeds.ui.components.BottomNavigationBar
import com.joaoreis.catbreeds.ui.components.Screen
import com.joaoreis.catbreeds.ui.theme.CatBreedsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val bottomNavScreens = listOf(Screen.AllCatBreeds, Screen.FavoriteCatBreeds)
            CatBreedsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController,
                            items = bottomNavScreens
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.AllCatBreeds.route,
                        modifier = Modifier.padding(paddingValues = paddingValues)
                    ) {
                        composable(Screen.AllCatBreeds.route) {
                            AllCatBreedsListScreen(
                                modifier = Modifier.fillMaxSize(),
                                onItemClicked = {
                                    navController.navigate(
                                        Screen.CatBreedDetails.buildNavRoute(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                        composable(Screen.FavoriteCatBreeds.route) {
                            FavoriteCatBreedsScreen(
                                modifier = Modifier.fillMaxSize(),
                                onItemClicked = {
                                    navController.navigate(
                                        Screen.CatBreedDetails.buildNavRoute(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                        composable(Screen.CatBreedDetails.route) {
                            CatBreedDetailsScreen(
                                modifier = Modifier.fillMaxSize(),
                                onBackPressed = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}