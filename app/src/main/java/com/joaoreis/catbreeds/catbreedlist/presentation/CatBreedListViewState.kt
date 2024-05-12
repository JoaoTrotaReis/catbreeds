package com.joaoreis.catbreeds.catbreedlist.presentation

sealed class CatBreedListViewState {
    data object Loading: CatBreedListViewState()

    data class Loaded(
        val catBreedItems: List<CatBreedViewItem>
    ): CatBreedListViewState()

    data object Error: CatBreedListViewState()
}

data class CatBreedViewItem(
    val image: String,
    val breedName: String
)