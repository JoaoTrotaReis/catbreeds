package com.joaoreis.catbreeds.favorites.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewModel
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewState
import com.joaoreis.catbreeds.favorites.presentation.toListItem
import com.joaoreis.catbreeds.ui.components.CatBreedsList

@Composable
fun FavoriteCatBreedsScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: FavoriteCatBreedsViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.subscribeToStateChanges()
    }

    when (val state = viewModel.viewState.collectAsState().value) {
        FavoriteCatBreedsViewState.Error -> {
            Text("There was an error")
        }

        is FavoriteCatBreedsViewState.Loaded -> {
            CatBreedsList(
                catBreedItems = state.catBreedItems.map { it.toListItem() },
                onFavoriteToggle = { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) },
                modifier = modifier
            )
        }

        FavoriteCatBreedsViewState.Loading -> {
            CircularProgressIndicator()
        }
    }
}