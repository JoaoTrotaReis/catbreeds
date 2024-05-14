package com.joaoreis.catbreeds.catbreedlist.presentation

import com.joaoreis.catbreeds.ui.components.CatBreedListItem

sealed class CatBreedListViewState {
    data object Loading: CatBreedListViewState()

    data class Loaded(
        val catBreedItems: List<CatBreedViewItem>
    ): CatBreedListViewState()

    data object Error: CatBreedListViewState()
}

data class CatBreedViewItem(
    val id: String,
    val image: String,
    val breedName: String,
    val isFavorite: Boolean
)

fun CatBreedViewItem.toListItem(): CatBreedListItem = CatBreedListItem(
    id = id,
    image = image,
    breedName = breedName,
    isFavorite = isFavorite
)
