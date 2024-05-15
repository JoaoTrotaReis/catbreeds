package com.joaoreis.catbreeds.details.presentation

import com.joaoreis.catbreeds.ui.components.CatBreedListItem

sealed class CatBreedDetailsViewState {
    data object Loading : CatBreedDetailsViewState()

    data class Loaded(
        val catBreedDetails: CatBreedDetails
    ) : CatBreedDetailsViewState()

    data object Error : CatBreedDetailsViewState()
}

data class CatBreedDetails(
    val id: String,
    val image: String,
    val breedName: String,
    val descripton: String,
    val origin: String,
    val temperament: List<String>,
    val isFavorite: Boolean
)

