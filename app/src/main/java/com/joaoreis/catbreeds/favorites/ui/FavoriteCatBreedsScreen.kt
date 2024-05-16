package com.joaoreis.catbreeds.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewModel
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewState
import com.joaoreis.catbreeds.favorites.presentation.toListItem
import com.joaoreis.catbreeds.ui.components.CatBreedsList

@Composable
fun FavoriteCatBreedsScreen(
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit
) {
    val viewModel: FavoriteCatBreedsViewModel = hiltViewModel()


    when (val state = viewModel.viewState.collectAsState().value) {
        FavoriteCatBreedsViewState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("There was an error loading your favorites :(")
            }
        }

        is FavoriteCatBreedsViewState.Loaded -> {
            if (state.catBreedItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .testTag("EmptyFavorites"),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("You have no favorites yet! Go and add some")
                }
            } else {
                CatBreedsList(
                    catBreedItems = state.catBreedItems.map { it.toListItem() },
                    onFavoriteToggle = { id, _ -> viewModel.removeFromFavorites(id) },
                    modifier = modifier,
                    onItemClicked = onItemClicked
                )
            }

        }

        FavoriteCatBreedsViewState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}