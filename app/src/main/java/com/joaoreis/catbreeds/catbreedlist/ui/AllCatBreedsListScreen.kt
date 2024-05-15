package com.joaoreis.catbreeds.catbreedlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewState
import com.joaoreis.catbreeds.catbreedlist.presentation.toListItem
import com.joaoreis.catbreeds.ui.components.CatBreedsList
import com.joaoreis.catbreeds.ui.components.SearchField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCatBreedsListScreen(
    modifier: Modifier = Modifier,
    onItemClicked: (String) -> Unit
) {
    val viewModel: CatBreedListViewModel = hiltViewModel()
    val text = rememberSaveable { mutableStateOf("") }

    Column {
        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            text = text.value,
            onTextChange = { text.value = it },
            placeHolder = "Search cat breeds",
            onCloseClicked = { text.value = ""
                             viewModel.loadCatBreeds()
                             },
            onSearchClicked = { viewModel.searchCatBreeds(it)} )

        when (val state = viewModel.viewState.collectAsState().value) {
            CatBreedListViewState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No results found")
                }
            }

            is CatBreedListViewState.Loaded -> {
                CatBreedsList(
                    catBreedItems = state.catBreedItems.map { it.toListItem() },
                    onFavoriteToggle = { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) },
                    modifier = modifier,
                    onItemClicked = onItemClicked
                )
            }

            CatBreedListViewState.Loading -> {
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

            CatBreedListViewState.EmptySearchResults ->  {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No results found")
                }
            }
        }
    }
}