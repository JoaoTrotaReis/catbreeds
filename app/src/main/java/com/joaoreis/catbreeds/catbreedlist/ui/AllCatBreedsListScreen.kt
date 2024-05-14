package com.joaoreis.catbreeds.catbreedlist.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewState
import com.joaoreis.catbreeds.catbreedlist.presentation.toListItem
import com.joaoreis.catbreeds.ui.components.CatBreedsList

@Composable
fun AllCatBreedsListScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: CatBreedListViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadCatBreeds()
    }

    when (val state = viewModel.viewState.collectAsState().value) {
        CatBreedListViewState.Error -> {
            Text("There was an error")
        }

        is CatBreedListViewState.Loaded -> {
            CatBreedsList(
                catBreedItems = state.catBreedItems.map { it.toListItem() },
                onFavoriteToggle = { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) },
                modifier = modifier
            )
        }

        CatBreedListViewState.Loading -> {
            CircularProgressIndicator()
        }
    }

}