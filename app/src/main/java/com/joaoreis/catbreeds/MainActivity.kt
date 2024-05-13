package com.joaoreis.catbreeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewState
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedViewItem
import com.joaoreis.catbreeds.catbreedlist.ui.CatBreedsListScreen
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewModel
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewState
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
            val screens = listOf(Screen.AllCatBreeds, Screen.FavoriteCatBreeds)
            CatBreedsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController = navController, items = screens) }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.AllCatBreeds.route,
                        modifier = Modifier.padding(paddingValues = paddingValues)) {
                        composable(Screen.AllCatBreeds.route) {
                            val viewModel: CatBreedListViewModel = hiltViewModel()
                            val viewState = viewModel.viewState.collectAsState().value
                            when(viewState) {
                                is CatBreedListViewState.Loaded -> {
                                    CatBreedsListScreen(
                                        viewState.catBreedItems,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                else -> {}
                            }
                        }
                        composable(Screen.FavoriteCatBreeds.route) {
                            val viewModel: FavoriteCatBreedsViewModel = hiltViewModel()
                            val viewState = viewModel.viewState.collectAsState().value
                            when(viewState) {
                                is FavoriteCatBreedsViewState.Loaded -> {
                                    CatBreedsListScreen(
                                        listOf(),//viewState.catBreedItems.map { CatBreedViewItem(it.image, it.breedName) },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}