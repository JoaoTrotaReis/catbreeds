package com.joaoreis.catbreeds.favorites.presentation

sealed class FavoriteCatBreedsViewState {
    data object Loading: FavoriteCatBreedsViewState()

    data class Loaded(
        val catBreedItems: List<FavoriteCatBreedViewItem>
    ): FavoriteCatBreedsViewState()

    data object Error: FavoriteCatBreedsViewState()
}

data class FavoriteCatBreedViewItem(
    val image: String,
    val breedName: String
)