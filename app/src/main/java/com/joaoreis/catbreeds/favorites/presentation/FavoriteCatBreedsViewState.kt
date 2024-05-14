package com.joaoreis.catbreeds.favorites.presentation

import com.joaoreis.catbreeds.ui.components.CatBreedListItem

sealed class FavoriteCatBreedsViewState {
    data object Loading : FavoriteCatBreedsViewState()

    data class Loaded(
        val catBreedItems: List<FavoriteCatBreedViewItem>
    ) : FavoriteCatBreedsViewState()

    data object Error : FavoriteCatBreedsViewState()
}

data class FavoriteCatBreedViewItem(
    val id: String,
    val image: String,
    val breedName: String
)

fun FavoriteCatBreedViewItem.toListItem(): CatBreedListItem = CatBreedListItem(
    id = id,
    image = image,
    breedName = breedName,
    isFavorite = true
)
